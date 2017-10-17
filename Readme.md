# Dynamic Ad Feed Converter
A simple, config driven application to convert input CSV files into a form suitable for consumption by [Google](https://support.google.com/merchants/answer/7052112?hl=en-GB) or [Facebook](https://developers.facebook.com/docs/marketing-api/dynamic-product-ads/product-catalog/) for their respective Ad Feeds.

## Config
A [sample config file](/src/main/resources/sample-config.json) is included in the project.  The syntax is quite basic:
```
{
  "sourceType": "GoogleDrive",
  "destType": "GoogleDrive",
  "sourceDir": "0B31u0-bAMcrhM2pYQzZPZXloN28",
  "destDir": "0B31u0-bAMcrhUGNVR2t2eFlEVzg",
  "destFileName": "AMC-Google.csv",
  "secretFileName": "client_secret.json",
  "outputFormat": "GoogleFeed",
  "mappings": {
    "id": "Source ID",
    "title": "Title",
    "description": "Description",
    "link": "Trailer Url",
    "image_link": "Thumbnail",
    "availability": "%STATIC:in stock",
    "google_product_category": "%STATIC:839",
    "price": "%STATIC:4.99 USD",
    "condition": "%STATIC:new",
    "brand": "%STATIC:Shudder"

  }
}
```

* sourceType - Where does the input file come from - currently supports `Local` or `GoogleDrive`
* destType - Where do we write the output - currently supports `Local` or `GoogleDrive`
* sourceDir - Either a locally accessible path, or a Google Drive folder ID
* destDir - Either a locally accessible path, or a Google drive folder ID
* destFileName - The file name that gets written and updated
* secretFileName - The path to your client_secret.json file - only required if interacting with GoogleDrive
* outputFormat - FacebookFeed or GoogleFeed
* SourceToDestMappings - The keys should be the output Google/Facebook field names, the values are their respective fields in the input file.  If you want to statically assign a value to all rows for a given field in the output, express it using `%STATIC:value` as shown.


## Execution
* There is a simple main method [included](/src/main/java/Entrypoint.java) in the project, along with an AWS Lambda Handler.
