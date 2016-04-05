Compiling and running
---------------------

We used both Java and Python in this assignment. To download all Maven dependencies and compile, run:
mvn package

Since many algorithms were quite slow to run on the full dataset, we ran the algorithms on a subset of data. We wrote copyFiles.java to copy a subset of data to run analysis on, and it is used like this:
java -jar target/Content-Enrichment-1.0-SNAPSHOT-jar-with-dependencies.jar copy <path of source directory> <path of destination directory>

For convenience, we used absolute paths in quite a few scripts with the intention of passing in paths from the command line in the final submission. However, as we got close to the due date, we became more hesitant to change them due to the risk of breaking something. As a result, quite a few scripts still use absolute paths that you must manually change before running them. They are:
getMeasurementUnits.sh,TTRParser.java,BibTextoJSONParser.java,generateGrobid.sh,generateRegex.py,generateSweet.sh,textToJSONParser.java,merged_features.py,metadata_score.py,getExifData.sh

Part 3:
# perform ttr parsing using following command
java -jar target/Content-Enrichment-1.0-SNAPSHOT-jar-with-dependencies.jar ttr <path to directory>
# Execute shell script from command prompt to generate measurements.json file.
sh getMeasurements.sh

Part 4:
# generate DOI_URLs.json which contains list of urls to be shortened.
java -jar target/Content-Enrichment-1.0-SNAPSHOT-jar-with-dependencies.jar doi <path to directory>
# Follow the instruction on http://yourls.org/#Install for initial setup.
# Setup php and database server. We used XAMPP for this assignment. Download link: https://www.apachefriends.org/index.html
# We used config.php configuration file for initial configuration.
# Upload urlsShortner.html and writeJSON.php on the server. It calls yourls.api.
# Run urlsShortner.html and click on generateURL button. You will get DOI.json as the output.

Part 5:
We run this program from Ubuntu's command prompt.
# compile BibTextoJSONParser.java using following command
javac BibtextoJSONParser.java
# Execute generateGrobid.sh file to generate grobid.json file.
sh generateGrobid.sh

Part 6:
Running this step requires setting the classpath, so we made the content-enrichment script to make it easier to run this. Run it like this:

./content-enrichment geo /path/to/files

The output is saved to geotopic.json. Note that the shell script may choke if there are spaces in the path you give it.

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

Part 9:
For better or worse, we have several slightly different versions of our program and schema to ingest JSON output into Solr 4.10.4. The original version is in the Part9And10 folder, but parts 11 and 12 use modified versions in their respective "solr cores" folders to store different JSON data in separate Solr cores.

Before running solr_ingest.py:
pip3 install --user SolrClient
Copy schema.xml to solr-4.10.4/example/solr/collection1/conf (replace collection1 with appropriate core name)
Start up solr and create cores called collection1, geotopic, grobid, measurements, and sweet.

Then cd into the directory that solr_ingest is in and run:
python3 ./solr_ingest.py

Then reload the Solr core.

In parts 11 and 12, you may have to copy schema.xml and run solr_ingest.py in each of the folders in "solr cores".

Part 10:
Annoyingly, the Memex GeoParser insists on running its own (quite slow) NER instead of using the already-extracted locations in our Solr index. We originally tried both importing our Solr index using the UI and writing a script to directly inject our NER extractions into GeoParser's Solr index, but after running into issues, the solution we eventually used was to save our location extractions to files in GeoParser's uploaded_files directory. This was less problematic than trying to import our Solr index, and lets NER extraction run much faster than giving GeoParser the raw Polar files. To run our script, cd into Part9And10 and run:

./memex_geoparser_ingest.py /path/to/GeoParser

Then reload the GeoParser web page.

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
