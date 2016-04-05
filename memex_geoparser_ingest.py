#!/usr/bin/env python3

import json
import sys
from SolrClient import SolrClient

if len(sys.argv) < 2:
    print("usage: ./memex_geoparser_ingest.py /path/to/GeoParser filename")
    sys.exit()
locations = set()
with open('geotopic.json', 'r') as f:
    for j in json.load(f).values():
        for k, v in j.items():
            if 'NAME' in k:
                locations.add(v)
        if len(locations) >= 200: break
with open(sys.argv[1] + '/geoparser_app/static/uploaded_files/' + sys.argv[2], 'w') as f:
    f.write('\n'.join(locations))
