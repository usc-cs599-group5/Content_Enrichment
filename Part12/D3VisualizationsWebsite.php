<?php

	header('Content-Type:text/html;charset=utf-8');

	//$query=isset($_REQUEST['q'])?$_REQUEST['q']:false;
	$query="*:*";
	$results=false;

	if($query&&isset($_REQUEST['q']))
	{
		if($_REQUEST['q']=="grobid"||$_REQUEST['q']=="geotopic"||$_REQUEST['q']=="sweet")
		{
		require_once('solr-php-client/Apache/Solr/Service.php');

		$solr=new Apache_Solr_Service('localhost',8983,'/solr/'.$_REQUEST['q']);
		
		if(get_magic_quotes_gpc()==1)
		{
			$query=stripslashes($query);
		}
		try
		{
			$additionalParameters=array(
				'indent'=>'true'			
			);	
			$results=$solr->search($query,$additionalParameters);
			$output=fopen($_REQUEST['q'].".json","w");
		}
		catch(Exception $e)
		{
			die("<html><head><title>SEARCH EXCEPTION</title><body><pre>{$e->__toString()}</pre></body></html>");
		}
		}
	}
?>

<html>
	<head>
		<title>Polar-data Visualizations</title>
	</head>
	<body>
		<form accept-charset="utf-8" method="get">
			<label for="q">Search:</label>
			<!--<input id="q" name="q" type="text" value="<?php echo htmlspecialchars($query,ENT_QUOTES,'utf-8');?>"/>-->
			<select name="q" id="q">
				<option value="grobid">Grobid Information(Based on Publications)</option>			
				<option value="metadatascores">Metadata Scores of files</option>			
				<option value="geotopic">Geo Locations found</option>			
				<option value="sweet">Sweet Ontologies Exctracted</option>			
				<option value="measurements1">Measurements units found in 100 files</option>			
				<option value="measurements2">Measurement Units Histogram of all files</option>			
			</select>
			<input type="submit"/>
		</form>
		<?php

			if($results)
			{
		?>
		<script>
			var file="<?php echo $_REQUEST['q']?>";
			window.open("http://localhost:786/"+file);
		</script>
		<?php
			}
		?>
	</body>
</html>
