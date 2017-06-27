INSERT schema.MusicAlbum (id, byArtist, name)
SELECT
   CONCAT("http://example.com/album/", CAST(a.album_id AS STRING)) AS id,
   STRUCT(
      CONCAT("http://example.com/artist/", CAST(b.group_id AS STRING)) AS id,
      b.group_name AS name
   ) AS byArtist,
   a.album_name AS name
FROM 
   schema.OriginMusicAlbumShape AS a
 JOIN
   schema.OriginMusicGroupShape AS b
 ON
   a.artist_id=b.group_id;
