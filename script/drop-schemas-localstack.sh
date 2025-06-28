#!/bin/bash

# Endpoints per LocalStack
ENDPOINT=http://localhost:4566
REGION=us-east-1

# Nome delle risorse
DYNAMO_TABLE=invoices
S3_BUCKET=dca-invoice-service

echo "🗑️ Cancellazione di tutti gli oggetti nel bucket S3: $S3_BUCKET"

# Prendi la lista degli oggetti (solo le chiavi)
OBJECT_KEYS=$(aws --endpoint-url=$ENDPOINT s3api list-objects-v2 \
  --bucket $S3_BUCKET \
  --region $REGION \
  --query "Contents[].Key" --output text)

if [ -z "$OBJECT_KEYS" ]; then
  echo "Il bucket S3 è già vuoto."
else
  for KEY in $OBJECT_KEYS; do
    aws --endpoint-url=$ENDPOINT s3api delete-object \
      --bucket $S3_BUCKET \
      --key "$KEY" \
      --region $REGION
  done
  echo "Tutti gli oggetti nel bucket sono stati cancellati."
fi

echo "🗑️ Eliminazione del bucket S3: $S3_BUCKET"
aws --endpoint-url=$ENDPOINT s3api delete-bucket \
  --bucket $S3_BUCKET \
  --region $REGION

echo "🗑️ Eliminazione della tabella DynamoDB: $DYNAMO_TABLE"
aws --endpoint-url=$ENDPOINT dynamodb delete-table \
  --table-name $DYNAMO_TABLE \
  --region $REGION

echo "✅ Eliminazione completa delle risorse completata."

read -n 1 -s -r -p "Premi un tasto per chiudere..."
echo
