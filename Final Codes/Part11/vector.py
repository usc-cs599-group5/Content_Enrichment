#!/usr/bin/env python2.7
#
# Licensed to the Apache Software Foundation (ASF) under one or more
# contributor license agreements.  See the NOTICE file distributed with
# this work for additional information regarding copyright ownership.
# The ASF licenses this file to You under the Apache License, Version 2.0
# (the "License"); you may not use this file except in compliance with
# the License.  You may obtain a copy of the License at
#
#    http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#
#

import math
from math import*
import os, editdistance, itertools, argparse, csv
import json

def stringify(attribute_value):
        if isinstance(attribute_value, list) and all(isinstance(x,dict) for x in attribute_value):
            return json.dumps(attribute_value)
        elif isinstance(attribute_value, list):
            return str((", ".join(attribute_value)).encode('utf-8').strip())
        else:
            return str(attribute_value.encode('utf-8').strip())



class Vector:
    '''
    An instance of this class represents a vector in n-dimensional space
    '''
    
    def __init__(self, features=None, filename=None):
        '''
        Create a vector
        @param metadata features 
        '''
        self.features = {}
        file_feature_value = {}
        

        if features:
            self.filename = filename

            na_metadata = ["resourceName"]
            for na in na_metadata:
                features.pop(na, None)

            for key in features:
                self.features[key] = len(stringify(features[key]))
                file_feature_value[key] = stringify(features[key])


    def stringify(attribute_value):
        if isinstance(attribute_value, list) and all(isinstance(x,dict) for x in attribute_value):
            return json.dumps(attribute_value)
        elif isinstance(attribute_value, list):
            return str((", ".join(attribute_value)).encode('utf-8').strip())
        else:
            return str(attribute_value.encode('utf-8').strip())


    def getMagnitude(self):
        totalMagnitude = 0.0
        for key in self.features:
            totalMagnitude += self.features[key] ** 2
        return math.sqrt(totalMagnitude)


    def dotProduct(self, anotherVector):
        dot_product = 0.0
        intersect_features = set(self.features) & set(anotherVector.features)
        
        for feature in intersect_features:
            dot_product += self.features[feature] * anotherVector.features[feature]
        return dot_product

    def compute_jaccard_index(self, v2):
        intersection_cardinality = len(set.intersection(*[set(self.features), set(v2.features)]))
        union_cardinality = len(set.union(*[set(self.features), set(v2.features)]))
        result = intersection_cardinality/float(union_cardinality)
        return (1-result)

    def cosTheta(self, v2):
        return self.dotProduct(v2) / (self.getMagnitude() * v2.getMagnitude())


    def euclidean_dist(self, anotherVector):
        '''
        dist = ((x1-x2)^2 + (y1-y2)^2 + (z1-z2)^2)^(0.5)
        '''
        intersect_features = set(self.features) & set(anotherVector.features)

        dist_sum = 0.0
        for feature in intersect_features:
            dist_sum += (self.features[feature] - anotherVector.features[feature]) ** 2

        setA = set(self.features) - intersect_features
        for feature in setA:
            dist_sum += self.features[feature] ** 2

        setB = set(anotherVector.features) - intersect_features
        for feature in setB:
            dist_sum += anotherVector.features[feature] ** 2

        return math.sqrt(dist_sum)