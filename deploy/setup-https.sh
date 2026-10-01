#!/usr/bin/env bash
# EC2에서 한 번 실행: nginx 리버스 프록시 + Let's Encrypt(certbot) 무료 인증서 발급
# Amazon Linux 2023 / Ubuntu 둘 다 지원
# 사용법: bash setup-https.sh <도메인> <이메일>
#   예)   bash setup-https.sh churai.duckdns.org me@example.com
# 사전 조건: 도메인이 EC2 탄력적 IP를 가리키고, 보안그룹에 80/443 인바운드가 열려 있어야 함
set -euo pipefail

DOMAIN=${1:?"사용법: bash setup-https.sh <도메인> <이메일>"}
EMAIL=${2:?"사용법: bash setup-https.sh <도메인> <이메일>"}

if command -v dnf > /dev/null; then
    # Amazon Linux 2023: sites-available 구조가 없고 conf.d/*.conf를 읽음
    sudo dnf install -y nginx certbot python3-certbot-nginx
    CONF=/etc/nginx/conf.d/churai.conf
else
    sudo apt-get update
    sudo apt-get install -y nginx certbot python3-certbot-nginx
    CONF=/etc/nginx/sites-available/churai
fi

# 80 → 127.0.0.1:8080(Spring) 프록시. certbot이 이 블록에 443 설정과 http→https 리다이렉트를 추가함
sudo tee "${CONF}" > /dev/null <<EOF
server {
    listen 80;
    server_name ${DOMAIN};

    # 5주차 이미지 업로드(최대 3장) 대비
    client_max_body_size 20M;

    location / {
        proxy_pass http://127.0.0.1:8080;
        proxy_set_header Host \$host;
        proxy_set_header X-Real-IP \$remote_addr;
        proxy_set_header X-Forwarded-For \$proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto \$scheme;
    }
}
EOF

if [ -d /etc/nginx/sites-enabled ]; then
    sudo ln -sf "${CONF}" /etc/nginx/sites-enabled/churai
    sudo rm -f /etc/nginx/sites-enabled/default
fi

sudo nginx -t
sudo systemctl enable --now nginx
sudo systemctl reload nginx

# 인증서 발급 + nginx에 443/리다이렉트 자동 적용
sudo certbot --nginx -d "${DOMAIN}" -m "${EMAIL}" \
    --agree-tos --no-eff-email --redirect --non-interactive

# 자동 갱신: Ubuntu는 설치 시 타이머가 켜지지만 Amazon Linux는 직접 켜야 함
if systemctl list-unit-files certbot-renew.timer > /dev/null 2>&1; then
    sudo systemctl enable --now certbot-renew.timer
fi
sudo certbot renew --dry-run

echo "완료: https://${DOMAIN}/api/v1/health"
