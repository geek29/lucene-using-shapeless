name := "lucene-using-shapeless"

version := "1.0"

scalaVersion := "2.12.2"

resolvers ++= Seq(
  Resolver.sonatypeRepo("releases"),
  Resolver.sonatypeRepo("snapshots")
)

libraryDependencies ++= Seq(
  "com.chuusai" %% "shapeless" % "2.3.2",
  "org.apache.lucene" % "lucene-core" % "6.5.0",
  "org.apache.lucene" % "lucene-analyzers-common" % "6.5.0",
  "org.apache.lucene" % "lucene-queries" % "6.5.0",
  "org.apache.lucene" % "lucene-queryparser" % "6.5.0"
)