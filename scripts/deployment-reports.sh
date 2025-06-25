if [[ "$#" -eq 0 ]]; then
  echo "Invalid parameters"
  echo "Command to deploy normalizar function: deployment.sh -n"
  echo "Command to deploy generatePDF function: deployment.sh -g"
  echo "Command to deploy descargar function: deployment.sh -d"
  exit 1      
fi

while [[ "$#" -gt 0 ]]; do
    case $1 in
        -n) normalizar=1 ;;
        -g) crear=1 ;;
        -d) descargar=1 ;;
        *) echo "Unknown parameter passed: $1"; exit 1 ;;
    esac
    shift
done

REGION=$(echo "us-east-1")

if [[ $normalizar -eq 1 ]] || [[ $crear -eq 1 ]]; then
  echo "Desplegando funciones java"

  cd ../server
  sam deploy --template tenant-reports.yaml --config-file tenant-reports.toml --region=$REGION

  cd ../scripts
fi


if [[ $normalizar -eq 1 ]]; then
  echo "Desplegando funciones java"

  cd ../server
  sam deploy --template tenant-reports.yaml --config-file tenant-reports.toml --region=$REGION

  cd ../scripts
fi
