#! /bin/bash -e

# Build (next static)
pnpm run build

# Copy to S3
aws s3 sync ./out s3://cathaleia-test --delete

# # Invalidate CloudFront cache
# aws cloudfront create-invalidation --distribution-id YOUR_DISTRIBUTION_ID_COPIED_FROM_PREVIOUS_STEP --paths "/*"