Segregated files using copyFiles

Part 3:
1) Created list of old urls using DOI_JSON.java.
2) Performed TTR parsing using TTRParser.java
3) Extracted measurements using getMeasurements.sh
4) measurements.json output

Part 4:
1) used yourls.org to get the initial set up done
2) used xampp as a local php and database server
3) urlsShortner.html calls the yourls's api and writeJSON.php
4) writeJSON.php generates the output JSON
5) config.php is the configuration file
6) DOI.json output

Part 5:
1) generateGrobid.sh calls nltk server and gets the author information and then using scholar.py to call google-scholar-api and get the related publications information
2) scholar.py returns results in BibTex to convert BibTex to JSON
3) grobid.json output

Part 7:
1) generateRegex.py generates the regex to be used in NER
2) regex.txt contains the regex generarted
3) generateSweet.sh calls NER and gets the sweet concepts in a text file sweet.txt
4) textToJSONParser.java generates JSON from sweet.txt
5) sweet.json output

Part 8:
1) merge_features.py merges the features of all the output jsons and creates merged_features.json
3) metadata_score.py calculates metadatsscores and stores in merged_features_with_score.json

Part 11:
1) argK-means.py reads json from solr and generates clusters of sweet,grobid and measurements in jsons
2) argK-meansForGeo does same for geotopic
3) .json outputs
4) solr cores solr schemas and indexing scripts
5) d3-d3 library files
6) .html output d3s

Part 12:
1) D3VisualizationWebsire.php webpage for selecting d3 visualization
2) used solr-php-client in 1
3) d3*_ contains d3 visualization files
4) solr cores solr schemas and indexing scripts

Part 14:
1) getExifData.sh calls exiftool parser
2) exif.json output