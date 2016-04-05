from itertools import izip
import json
from datetime import datetime

def main():
    start_time = datetime.now()
    data_files_paths = [
        'E:\Sem2\CSCI599\Assignment2\Others\Part8\outputs\doi.json',
        'E:\Sem2\CSCI599\Assignment2\Others\Part8\outputs\grobid.json',
        'E:\Sem2\CSCI599\Assignment2\Others\Part8\outputs\geotopic.json',
        'E:\Sem2\CSCI599\Assignment2\Others\Part8\outputs\exif.json',
        'E:\Sem2\CSCI599\Assignment2\Others\Part8\outputs\file_size_data.json']

    error_file = open('errors.txt', 'w+')
    merged_features_file = open('merged_features.json', 'w+')
    i = 0
    with open(data_files_paths[0]) as opennlp_data, open(data_files_paths[1]) as grobid_data, open(
            data_files_paths[2]) as geotopic_data, open(data_files_paths[3]) as exiftool_data, open(
        data_files_paths[4]) as file_size_data:
        for a, b, c, d, e, g in izip(opennlp_data, grobid_data, geotopic_data, exiftool_data, file_size_data):
            a = json.loads(a.strip())
            b = json.loads(b.strip())
            c = json.loads(c.strip())
            d = json.loads(d.strip())
            e = json.loads(g.strip())
            i += 1
            if a['id'] == b['id'] and b['id'] == c['id'] and c['id'] == d['id'] and d['id'] == e['id'] and a['Content-Type'] == b['Content-Type'] and b['Content-Type'] == c[
                'Content-Type'] and c['Content-Type'] == d['Content-Type'] and d['Content-Type'] == e['Content-Type']:
                merged_json = {}
                for key, value in a.iteritems():
                    merged_json[key] = value
                for key, value in b.iteritems():
                    merged_json[key] = value
                for key, value in c.iteritems():
                    merged_json[key] = value
                for key, value in d.iteritems():
                    merged_json[key] = value
                for key, value in e.iteritems():
                    merged_json[key] = value
                json.dump(merged_json, merged_features_file)
                merged_features_file.write('\n')
            else:
                error_file.write('Error in line ' + str(i) + ':\n')
                error_file.write(
                    'opennlp_data[id]: ' + a['id'] + '  - opennlp_data[Content-Type]:' + a['Content-Type'] + '\n')
                error_file.write(
                    'grobid_data[id]: ' + b['id'] + '  - grobid_data[Content-Type]:' + b['Content-Type'] + '\n')
                error_file.write(
                    'geotopic_data[id]: ' + c['id'] + '  - geotopic_data[Content-Type]:' + c['Content-Type'] + '\n')
                error_file.write(
                    'exiftool_data[id]: ' + d['id'] + '  - exiftool_data[Content-Type]:' + d['Content-Type'] + '\n')
                error_file.write(
                    'file_size_data[id]: ' + e['id'] + '  - file_size_data[Content-Type]:' + e['Content-Type'] + '\n')

    error_file.close()
    end_time = datetime.now()
    print(end_time - start_time)
    print('Merged ' + str(i) + ' files')


if __name__ == '__main__':
    main()
