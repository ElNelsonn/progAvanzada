@echo off
echo --- Stopping containers ---
docker compose down

echo --- Rebuilding & starting ---
docker compose up -d --build

echo --- Deployment complete ---
