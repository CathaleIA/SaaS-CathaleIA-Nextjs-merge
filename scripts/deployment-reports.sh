  cd ../server
  REGION=$(echo "us-east-1")
  sam build -t tenant-reports.yaml
  sam deploy --config-file tenant-reports.toml --region=$REGION