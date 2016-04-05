<?php
$doi_json=fopen("DOI_JSON.json","w");
fwrite($doi_json,$_GET['urls']);
fclose($doi_json);
echo $_GET['urls'];
?>