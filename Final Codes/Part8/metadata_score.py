from itertools import izip
import json
from datetime import datetime

from scipy.weave.ext_tools import indent

def no_of_fields_score(json_line):
    count = 0
    score = 0.0
    for key in json_line:
        count += 1
    if count<20:
        score = count/20.0
    if count >= 20:
        score =1
    return score

def get_metadata_score(json_line):
    metadata_score = 0
    if 'description' in json_line:
        metadata_score += 0.5
    if 'title' in json_line:
        metadata_score+=0.25
    if 'version' in json_line:
        metadata_score+=0.25
    if 'license' in json_line:
        metadata_score+=1
    #calculates scores based on the keys in the json file
    countscore = no_of_fields_score(json_line)
    metadata_score += countscore
    #adds DOI score
    metadata_score+=1
    #for long term management constant score 1
    metadata_score+=1
    #normalizing the score
    metadata_score = '%.3f'%(metadata_score/6)    
    print "metadata score is ", metadata_score
    print(json.dumps(json_line, indent=4))
    return metadata_score


    
    
def main():
    start_time = datetime.now()
    merged_file_path = 'E:/Sem2/CSCI599/Assignment2/Part8/merged_features.json'

    merged_features_file_with_score = open('merged_features_with_score.json', 'w+')
    i = 0
    with open(merged_file_path) as merged_data:
        for line in merged_data:
            json_line = json.loads(line.strip())
            i += 1
            json_line['metadata_score'] = get_metadata_score(json_line)
            print ('Parsed ' + str(i) + ' files')
            json.dump(json_line, merged_features_file_with_score)
            merged_features_file_with_score.write('\n')
            
    end_time = datetime.now()
    print(end_time - start_time)
    print('Merged ' + str(i) + ' files')


if __name__ == '__main__':
    main()
