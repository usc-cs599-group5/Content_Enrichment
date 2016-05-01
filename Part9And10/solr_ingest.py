#!/usr/bin/env python3

"""
setup before running this:
pip3 install --user SolrClient
download and extract solr 4.10 from https://archive.apache.org/dist/lucene/solr/4.10.4/solr-4.10.4.tgz
copy schema.xml to solr-4.10.4/example/solr/collection1/conf
bin/solr start

solr_ingest.py indexes these files: measurements.json, DOI.json, grobid.json, geotopic.json, sweet.json

schema docs: https://cwiki.apache.org/confluence/display/solr/Documents%2C+Fields%2C+and+Schema+Design
clear solr index: https://wiki.apache.org/solr/FAQ#How_can_I_delete_all_documents_from_my_index.3F
"""

import json
from collections import defaultdict
from SolrClient import SolrClient

solr = SolrClient('http://localhost:8983/solr')
j = defaultdict(dict)
with open('DOI.json', 'r') as f:
    for k, v in json.load(f).items():
        j[k]['doi'] = v
with open('grobid.json', 'r') as f:
    for doc in json.load(f):
        for i, pub in enumerate(doc['relatedPublications']):
            for k, v in pub.items():
                j[doc['id']]['relatedPublications_' + k + '_' + str(i)] = v
with open('geotopic.json', 'r') as f:
    for k, v in json.load(f).items():
        j[k].update(v)
with open('measurements.json', 'rb') as f:
    for doc in json.loads(f.read().decode(errors='ignore')):
        j[doc['id']]['units'] = doc['units']
with open('sweet.json', 'r') as f:
    for doc in json.load(f):
        for k, v in doc.items():
            if k.startswith('NER_Sweet_'):
                j[doc['id']][k] = v
for k, v in j.items():
    v['id'] = k
solr.index_json('collection1', json.dumps(list(j.values())))
