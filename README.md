# geo-search-service
a REST service for doing geo search

This is a quickk and dirty REST service I set up that does some pretty useful geo searching. I expose two calls in the api:
 
```
/namelookup?name=place_name_here


/latlon?lat=23.456&lon=43.567
```

They return some simple json strings in the body of the response. Among the data that come back are the polygon rings and printable names for different geographic areas. I.e. you could use the gos info from a tablet or phone and display its location on a map with the surrounding geographic area named and colored.

Here are some instructions to get you up and running.

The first thing you're going to need is some data. I downloaded the shapefile database for the countries of the world from [GADM.org](http://gadm.org/version2) The data consists of official administrative levels and their corresponding boundaries. The boundaries are cooridinate descriptions of the polygons that make up the boundaries of whatever geographic area you're looking at.  There are 5 or 6 levels, the higher the number the smaller the area, so adm level 0 are country borders and adm level 4 might be cities and towns. There is also some meta data associated with each entry, like its name in particular languages. A full description is available on the GADM site. In any case, you should unzip the file into a directory. There are lots of files and there is a lot of data.

In order to compute with the data, we're gonna need to put it into a postgres database with postgis extensions. Assuming you have all that set up on your computer, the steps are:

```
1. create a postgres db (from a postgres shell type: 'cretate database gadm0' or whatever you want to call it)
2. connect to that db (from a postgres shell type: \c gadm0)
3. create postgis extensions on that db (from a postgres shell type: create extension postgis)
4. create a sql file from which to load the data (I use shp2pgsql, from a bash prompt type: shp2pgsql -I -s 4326 gadm28_adm0.shp gadm0 >import_gadm.sql)
5. import the db with psql (from a bash prompt type: psql -D gadm0 -f import_gadm0.sql 
```


If you have a different way, great! You might have issues importing the data, but if you have the software installed, it's pretty quick. Once you have data in the DB, you can clone the repo on your computer. I open the repo as a gradle project with intelliJ. Gradle will find and install all the dependencies for you, so compiling and running the server are easy. You can start it with intelliJ as well. Navigate to the file /src/main/java/Server.java and run the main. Remember to set the database credentials to whatever you have set up. They are in the LoadData() method in the GeoSearchManager class.

Jumping back on a command line, you can test both of the rest calls like this:

```
$> curl "localhost:4580/namelookup?name=Афганистан"
$> curl "localhost:4580/namelookup?name=阿富汗"
$> curl "localhost:4580/latlon?lat=34.553&lon=69.507"
```


