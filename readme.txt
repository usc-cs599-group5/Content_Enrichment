Compiling and running
---------------------

We programmend in Java 8 and used Maven as our build system. We used Tika version 1.12. To download all dependencies and compile, run:
mvn package

An important thing to note is we run the algorithms on subset of data. We use "copyFiles.java" to generate lists of files to process, then we run the algorithms using these file lists. The motivation behind this is explained in our report. 
java -jar target/Content-Enrichment-1.0-SNAPSHOT-jar-with-dependencies.jar copy <path of source directory> <path of destination directory>
Then to run, execute following commands for different parts:

Part 3:
# generate DOI_URLs.json which contains list of urls to be shortened.
java -jar target/Content-Enrichment-1.0-SNAPSHOT-jar-with-dependencies.jar doi <path to directory>
# perform ttr parsing using following command
java -jar target/Content-Enrichment-1.0-SNAPSHOT-jar-with-dependencies.jar ttr <path to directory>
# Execute shell script from command prompt to generate measurements.json file.
sh getMeasurements.sh

Part 4:
# Follow the instruction on http://yourls.org/#Install for initial setup.
# Setup php and database server. We used XAMPP for this assignment. Download link :https://www.apachefriends.org/index.html
# We used config.php configuration file for initial configuration.
# Upload urlsShortner.html and writeJSON.php on the server. It calls yourls.api.
# Run urlsShortner.html and click on generateURL button. You will get DOI.json as the output.

Part 5:
We run this program from Ubuntu's command prompt.
# compile BibTextoJSONParser.java using following command
javac BibtextoJSONParser.java
# Execute generateGrobid.sh file to generate grobid.json file.
sh generateGrobid.sh

Part 7:
# generate regex file to be used in NER
python generateRegex.py > regex.txt
# Follow the instruction of Using Regular Expression from https://wiki.apache.org/tika/TikaAndNER
# Run following command to get sweet.txt file
sh generateSweet.sh > sweet.txt
# Generate sweet.json file from sweet.txt
java -jar target/Content-Enrichment-1.0-SNAPSHOT-jar-with-dependencies.jar tJParser [Path to sweet.txt]

Part 8:
# merge features from different json outputs and create merged_features.json.
python merge_features.py
# generate merged_features_with_score.json that contains metadata score.
python metadata_score.py

Part 11:
# To setup the Solr cores for measurement extractions, related publications and authors ,extracted locations and SWEET features follow the instruction on the given link and name them as measurement,grobid,geotopic and sweet respectively
https://www.codeenigma.com/host/faq/how-do-i-create-solr-core-my-server
# Replace Schema.xml files to their corresponding conf folder for their cores.
# Generate index
python solr_ingest.py
# generate clusters using K-means and tika-similarity for sweet,grobid and measurements
python argK-means.py --inCore CORENAME --outJSON OUTJSON --Kvalue KVALUE
# generate clusters for geotopic
python argK-meansForGeo.py --inCore CORENAME --outJSON OUTJSON --Kvalue KVALUE
# You can visualize the clusters using k-means-geotopic.html,k-means-grobid.html,k-means-sweet.html and k-means-measurements.html
NOTE: Make sure that d3 folder is in the same directory as k-means-*.html files and the OUTJSON files are named geo-clusters.json,grobid-clusters.json,measurements-clusters.json and sweet-clusters.json.

Part 12:
# Load all files in Part 12 folder to the htdocs folder of your localhost/apache folder.
# D3 Visualizations are in the d3*_ folders.
NOTE: Same Solr cores are used as in Part 11.

Part 14:
# Perform initial setup using http://wiki.apache.org/tika/EXIFToolParser
# generate exif.json by executing following command
sh getExifData.sh

NOTE : Following files contains absolute path which needs to be changed according to the requirement.
getMeasurementUnits.sh,TTRParser.java,BibTextoJSONParser.java,generateGrobid.sh,generateRegex.py,generateSweet.sh,textToJSONParser.java,merged_features.py,metadata_score.py,getExifData.sh

