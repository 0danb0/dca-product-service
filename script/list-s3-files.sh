#!/bin/bash

ENDPOINT=http://localhost:4566
BUCKET=dca-invoice-service

echo "📋 Lista oggetti nel bucket S3: $BUCKET"

aws --endpoint-url=$ENDPOINT s3 ls s3://$BUCKET --recursive

read -n 1 -s -r -p "Premi un tasto per chiudere..."
echo
