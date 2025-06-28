#!/bin/bash

# Endpoints per LocalStack
ENDPOINT=http://localhost:4566
REGION=us-east-1

# Nome delle risorse
DYNAMO_TABLE=invoices
S3_BUCKET=dca-invoice-service

echo "🗑️ Cancellazione di tutti gli elementi nella tabella DynamoDB: $DYNAMO_TABLE"

# Scan della tabella per ottenere tutte le chiavi (pk, sk)
ITEMS=$(aws --endpoint-url=$ENDPOINT dynamodb scan \
  --table-name $DYNAMO_TABLE \
  --region $REGION \
  --projection-expression "pk, sk" \
  --output json)

# Estrazione chiavi e cancellazione item per item
KEYS=$(echo $ITEMS | jq -c '.Items[]')

if [ -z "$KEYS" ]; then
  echo "La tabella è già vuota."
else
  for ITEM in $(echo "$KEYS"); do
    PK=$(echo $ITEM | jq -r '.pk.S')
    SK=$(echo $ITEM | jq -r '.sk.S')

    aws --endpoint-url=$ENDPOINT dynamodb delete-item \
      --table-name $DYNAMO_TABLE \
      --key "{\"pk\": {\"S\": \"$PK\"}, \"sk\": {\"S\": \"$SK\"}}" \
      --region $REGION
  done
  echo "Tutti gli elementi nella tabella sono stati cancellati."
fi

echo "🗑️ Cancellazione di tutti gli oggetti nel bucket S3: $S3_BUCKET"

# Lista oggetti nel bucket e cancellazione
OBJECTS=$(aws --endpoint-url=$ENDPOINT s3api list-objects-v2 \
  --bucket $S3_BUCKET \
  --region $REGION \
  --output json)

KEYS_TO_DELETE=$(echo $OBJECTS | jq -r '.Contents[].Key')

if [ -z "$KEYS_TO_DELETE" ]; then
  echo "Il bucket S3 è già vuoto."
else
  for KEY in $KEYS_TO_DELETE; do
    aws --endpoint-url=$ENDPOINT s3api delete-object \
      --bucket $S3_BUCKET \
      --key "$KEY" \
      --region $REGION
  done
  echo "Tutti gli oggetti nel bucket sono stati cancellati."
fi

echo "✅ Pulizia completata."

read -n 1 -s -r -p "Premi un tasto per chiudere..."
echo
