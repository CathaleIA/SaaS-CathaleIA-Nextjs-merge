#!/bin/bash

if [[ "$#" -eq 0 ]]; then
  echo "Invalid parameters"
  echo "Command to deploy client code: deployment.sh -c --email <email address>"
  echo "Command to deploy server code: deployment.sh -s --email <email address>"
  echo "Command to deploy server & client code: deployment.sh -s -c --email <email address>"
  exit 1
fi

while [[ "$#" -gt 0 ]]; do
  case $1 in
  -s) server=1 ;;
  -c) client=1 ;;
  --email)
    email=$2
    shift
    ;;
  *)
    echo "Unknown parameter passed: $1"
    exit 1
    ;;
  esac
  shift
done


# During AWS hosted events using event engine tool
IS_RUNNING_IN_EVENT_ENGINE=false


if [ "$IS_RUNNING_IN_EVENT_ENGINE" = false ]; then
  ADMIN_SITE_URL=$(aws cloudformation describe-stacks --stack-name serverless-saas --query "Stacks[0].Outputs[?OutputKey=='AdminAppSite'].OutputValue" --output text)
  LANDING_APP_SITE_URL=$(aws cloudformation describe-stacks --stack-name serverless-saas --query "Stacks[0].Outputs[?OutputKey=='LandingApplicationSite'].OutputValue" --output text)
  ADMIN_SITE_BUCKET=$(aws cloudformation describe-stacks --stack-name serverless-saas --query "Stacks[0].Outputs[?OutputKey=='AdminSiteBucket'].OutputValue" --output text)
  LANDING_APP_SITE_BUCKET=$(aws cloudformation describe-stacks --stack-name serverless-saas --query "Stacks[0].Outputs[?OutputKey=='LandingApplicationSiteBucket'].OutputValue" --output text)
  APP_SITE_BUCKET=$(aws cloudformation describe-stacks --stack-name serverless-saas --query "Stacks[0].Outputs[?OutputKey=='ApplicationSiteBucket'].OutputValue" --output text)
  APP_SITE_URL=$(aws cloudformation describe-stacks --stack-name serverless-saas --query "Stacks[0].Outputs[?OutputKey=='ApplicationSite'].OutputValue" --output text)
fi

if [[ $client -eq 1 ]]; then
  if [[ -z "$email" ]]; then
    echo "Please provide email address to setup an admin user"
    exit 1
  fi

  echo "Configuring environment for App Client"

  # Despliegue Admin UI
  cat <<EoF >.env.production
NEXT_PUBLIC_API_GATEWAY_URL="$ADMIN_APIGATEWAYURL"
NEXT_PUBLIC_AWS_REGION="$REGION"
EoF

# Limpiar caches y builds anteriores
rm -rf .next/ node_modules/ out/

# Instalar dependencias limpiando cache
npm cache clean --force
npm install --force

npm run build
aws s3 sync --delete --cache-control no-store ./out "s3://${APP_SITE_BUCKET}"

  echo "Deployment completed successfully"
#   cat <<EoF >./src/aws-exports.ts
# const awsmobile = {
#     "aws_project_region": "$REGION",
#     "aws_cognito_region": "$REGION",
#     "aws_user_pools_id": "$ADMIN_USERPOOL_ID",
#     "aws_user_pools_web_client_id": "$ADMIN_APPCLIENTID",
# };

# export default awsmobile;
# EoF

  # npm install && npm run build

  # echo "aws s3 sync --delete --cache-control no-store dist s3://${ADMIN_SITE_BUCKET}"
  # aws s3 sync --delete --cache-control no-store dist "s3://${ADMIN_SITE_BUCKET}"

  # Despliegue Landing UI (Next.js)
  # cd ../
  # cd Landing || exit

#   echo "Configuring environment for Landing Client"
#   cat <<EoF >.env.production
# NEXT_PUBLIC_API_GATEWAY_URL="$ADMIN_APIGATEWAYURL"
# NEXT_PUBLIC_AWS_REGION="$REGION"
# EoF

# # Limpiar caches y builds anteriores
# rm -rf .next/ node_modules/ out/

# # Instalar dependencias limpiando cache
# npm cache clean --force
# npm install --force
# npm install --save-dev @types/next postcss autoprefixer  # <-- Añade tailwindcss y autoprefixer

# npm run build
# aws s3 sync --delete --cache-control no-store ./out "s3://${LANDING_APP_SITE_BUCKET}"

#   echo "Deployment completed successfully"
fi

echo "Admin site URL: https://$ADMIN_SITE_URL"
echo "Landing site URL: https://$LANDING_APP_SITE_URL"