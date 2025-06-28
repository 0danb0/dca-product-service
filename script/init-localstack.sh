#!/bin/bash

# Endpoints per LocalStack
ENDPOINT=http://localhost:4566
REGION=us-east-1

# Nome delle risorse
DYNAMO_TABLE=InvoiceTable
S3_BUCKET=my-invoice-bucket

echo "✅ Creazione tabella DynamoDB: $DYNAMO_TABLE"

aws --endpoint-url=$ENDPOINT dynamodb create-table \
  --table-name $DYNAMO_TABLE \
  --attribute-definitions \
    AttributeName=pk,AttributeType=S \
    AttributeName=sk,AttributeType=S \
  --key-schema \
    AttributeName=pk,KeyType=HASH \
    AttributeName=sk,KeyType=RANGE \
  --billing-mode PAY_PER_REQUEST \
  --region $REGION

echo "✅ Creazione bucket S3: $S3_BUCKET"

aws --endpoint-url=$ENDPOINT s3api create-bucket \
  --bucket $S3_BUCKET \
  --region $REGION \
  --create-bucket-configuration LocationConstraint=$REGION

echo "✅ Operazione completata"

read -n 1 -s -r -p "Premi un tasto per chiudere..."
echo