#!/usr/bin/env python

import json
import sys
from SolrClient import SolrClient

if len(sys.argv) < 2:
    print("usage: ./memex_geoparser_ingest.py /path/to/GeoParser domain_name")
    print("GeoParser's Solr instance must be running when this script is executed.")
    sys.exit()
# convert locations into GeoParser format
points = []
locations = {}
with open('geotopic.json', 'r') as f:
    for j in json.load(f).itervalues():
        for k, loc_name in j.iteritems():
            if 'NAME' in k:
                x = j[k.replace('NAME', 'LATITUDE')] # x and y seem switched but this is how GeoParser stores it
                y = j[k.replace('NAME', 'LONGITUDE')]
                points.append({
                    'loc_name': loc_name,
                    'position': {
                        'x': x,
                        'y': y,
                    },
                })
                locations[loc_name] = [x, y]
        if len(points) >= 100: break
# add points to GeoParser's Solr index
solr = SolrClient('http://localhost:8983/solr')
solr.index_json('uploaded_files', json.dumps([{
    'id': sys.argv[2],
    'text': '',
    'locations': str(locations),
    'points': str(points),
}]))
solr.commit('uploaded_files')
# create blank file in uploaded_files directory so GeoParser knows where to look in Solr
with open(sys.argv[1] + '/geoparser_app/static/uploaded_files/' + sys.argv[2], 'w'):
    pass
