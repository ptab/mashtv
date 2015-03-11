# MashTV [![Build Status](https://travis-ci.org/ptab/mashtv.svg?branch=master)](https://travis-ci.org/ptab/mashtv)

### TODO
#### Features
* select shows from a list, not from user input
* show page
* match daily shows (with date instead of _S01E02_)

#### Issues
* episodes with . in the name are screwed . matches the link, but doesn't find it on DB

#### Code quality
* understand why swagger needs `@RequestMapping(method = Request.method)` even though it's the default
* checkstyle gradle plugin
* publish those reports somewhere
* add link to coveralls.io
* parameterize feed unit tests