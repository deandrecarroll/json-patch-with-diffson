# JSON PATCH with Diffson

[Diffson](https://github.com/gnieh/diffson) is a Scala library implementing [RFC-6901](http://tools.ietf.org/html/rfc6901), [RFC-6902](http://tools.ietf.org/html/rfc6902), and [RFC-7396](http://tools.ietf.org/html/rfc7396), systems for modifying JSON document bodies over HTTP PATCH. 

A brief, user firendly overview of the JSON PATCH structure can be found at [http://jsonpatch.com/](http://jsonpatch.com/). A terse description of how to use Diffson can be found in the README.md at its GitHub repository https://github.com/gnieh/diffson.

This example is meant to guide a user through incorporating and using Diffson with the [Play! Framework](https://www.playframework.com/). The project in this example is using Play! version 2.7.3.

## Adding Diffson via sbt

First things first, Diffson can be included in the the `build.sbt` file by adding the following line...

```
val jsonLib = "play-json"
...

libraryDependencies += "org.gnieh" %% f"diffson-$jsonLib" % "4.0.0-M3"
```

Note: Play! 2.7.3, using a standard Scala giter8 template initializes with a `scalaVersion` set to "2.13.0". The most current version of Diffson supports "2.11.x" and "2.12.x". Be sure to set the `scalaVersion` variable in the `build.sbt` file to prevent build errors.

## Example in the Scala worksheet

There is a Scala worksheet in this repository at [app/controllers/PatchProcess.sc](https://github.com/deandrecarroll/json-patch-with-diffson/blob/master/app/controllers/PatchProcess.sc) that will guide through the process of using Diffson with `play-json`.
