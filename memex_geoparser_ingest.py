#!/usr/bin/env python3

import json
import sys

partition = 200

if len(sys.argv) < 2:
    print("usage: ./memex_geoparser_ingest.py /path/to/GeoParser")
    sys.exit()
# extract set of unique locations from geotopic.json
locations = set()
with open('geotopic.json', 'r') as f:
    for j in json.load(f).values():
        for k, v in j.items():
            if 'NAME' in k:
                locations.add(v)
# partition into several files because GeoParser chokes on large files
locations = list(locations)
for i in range(len(locations) // partition):
    with open(sys.argv[1] + '/geoparser_app/static/uploaded_files/' + str(i), 'w') as f:
        f.write('\n'.join(locations[i*partition : (i+1)*partition]))
