# Dynamic Ad Feed Converter
A simple, config driven application to convert input CSV files into a form suitable for consumption by [Google](https://support.google.com/merchants/answer/7052112?hl=en-GB) or [Facebook](https://developers.facebook.com/docs/marketing-api/dynamic-product-ads/product-catalog/) for their respective Ad Feeds.

## Config
A [sample config file](/src/main/resources/sample-config.json) is included in the project.  The syntax is quite basic:
```
{
  "SourceDir": "/Users/James/Developer/dynamic-ad-feed-converter/sample-data.csv",
  "DestDir": "/Users/James/Developer/dynamic-ad-feed-converter/out.csv",
  "DestType": "GoogleFeed",
  "SourceToDestMappings": {
    "id": "Source ID",
    "title": "Title",
    "description": "Short Description",
    "link": "Trailer Url",
    "image_link": "Thumbnail",
    "availability": "%STATIC:in stock",
    "google_product_category": "Media Type",
    "price": "%STATIC:200 USD"
  }
}
```

* SourceDir - where does the input file come from
* DestDir - where do we write the output
* DestType - GoogleFeed or FacebookFeed
* SourceToDestMappings - The keys should be the output Google/Facebook field names, the values are their respective fields in the input file.  If you want to statically assign a value to all rows for a given field in the output, express it using `%STATIC:value` as shown.


## Execution
* There is a simple main method [included](/src/main/java/Entrypoint.java) in the project, along with an AWS Lambda Handler.
