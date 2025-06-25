cd ../server
REGION=$(echo "us-east-1")
sam deploy --template tenant-reports.yaml --config-file tenant-reports.toml --region=$REGION
