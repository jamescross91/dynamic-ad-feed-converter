# Dynamic Ad Feed Converter
A simple, config driven application to convert input CSV files into a form suitable for consumption by [Google](https://support.google.com/merchants/answer/7052112?hl=en-GB) or [Facebook](https://developers.facebook.com/docs/marketing-api/dynamic-product-ads/product-catalog/) for their respective Ad Feeds.

## Config
A [sample config file](/src/main/resources/sample-config.json) is included in the project.  The syntax is quite basic:
```
{
  "sourceType": "GoogleDrive",
  "fileType": "CSV",
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

### Mappings
The keys should be the output Google/Facebook field names, the values are their respective fields in the input file.
* If you want to statically assign a value to all rows for a given field in the output, express it using `%STATIC:value` as shown
* You can also combine multiple fields using the `%COMBINER: ` syntax: `"brand": "%COMBINER:Trailer Url, Thumbnail, Title"`.  This will append multiple fields together delimeted by a space.


## Execution
* There is a simple main method [included](/src/main/java/Entrypoint.java) in the project, along with an AWS Lambda Handler.

## XML Feeds

XML feeds can also be parsed - set fileType to XML:
```
{
  "SourceDir": "0B31u0-bAMcrhM2pYQzZPZXloN28",
  "sourceType": "URL",
  "fileType": "XML",
  "destType": "Local",
  "sourceDir": "https://www.internet.com/my-feedxml",
  "destDir": "/Users/James/Developer/dynamic-ad-feed-converter",
  "destFileName": "AMC-Google.csv",
  "secretFileName": "client_secret.json",
  "outputFormat": "GoogleFeed",
  "mappings": {
    "id": "contentId",
    "title": "title",
    "description": "description",
    "link": "image",
    "image_link": "image",
    "availability": "%STATIC:in stock",
    "google_product_category": "%STATIC:839",
    "price": "%STATIC:4.99 USD",
    "condition": "%STATIC:new",
    "brand": "%STATIC:shudder"
  },
  "xPathRoot": "/umcCatalog[@version=\"1.1\"]/channel/item",
  "xPathQueries" : {
    "contentId": "contentId",
    "pubDate": "pubDate",
    "title": "title",
    "description": "description",
    "image": "artwork/@url"
  }
}
```

Where:
* xPathRoot is the root element in the list of items to be processed - see example below
* xPathQueries query specific elements underneath the root element

The example config can be used to query elements in the sample data below, where `item` is the root element, and the queries in`xPathQueries` are used to extract data from an items child nodes or attributes.

### Sample XML Data
```
<umcCatalog version="1.1">
<teamId>72GP743PB7</teamId>
<channel>
<lastBuildDate>2017-11-19T00:37:36+00:00</lastBuildDate>
<catalogId>com.sundancenow.shudder.catalog</catalogId>
<item>
<contentType>tv_episode</contentType>
<contentId>3046099</contentId>
<pubDate>2017-10-24T14:48:21+00:00</pubDate>
<title>2. Hope</title>
...
<item>
<contentType>tv_episode</contentType>
<contentId>123456</contentId>
<pubDate>2017-12-24T14:48:21+00:00</pubDate>
<title>Another title</title>
```