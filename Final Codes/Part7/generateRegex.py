import os, os.path
import rdflib
import sys
import json
from rdflib import Namespace

g=rdflib.Graph()
for root, dirs, files in os.walk("E://Sem 2//CSCI 599//Assignment 2//OWLFiles//2.3"):
    for f in files:
        fullpath = os.path.join(root, f)
        if os.path.splitext(fullpath)[1] == '.owl':
			g.parse('file:///'+fullpath)
			d = {}
			for s,p,o  in g:
				try:
					subject = s.split("#")[1]
					predicate = p.split("#")[1]
					obj = o.split("#")[1]
					if obj == 'Class':
						continue
					if obj in d:
						d[obj].append(subject)
					else:
						d[obj] = []
						d[obj].append(subject)

				except:
					continue

for key in d.keys():
	sys.stdout.write("Sweet_"+key+"=(")
	for i in range(len(d[key])):
		if i == 0:
			sys.stdout.write(d[key][i])
		else:
			sys.stdout.write("|"+d[key][i])
		print ")"