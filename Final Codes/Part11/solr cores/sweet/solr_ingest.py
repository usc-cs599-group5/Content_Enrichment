#!/usr/bin/env python3

from SolrClient import SolrClient

solr = SolrClient('http://localhost:8983/solr')
solr.local_index('sweet', 'sweet.json')
