THIS FILE IS WORK IN PROGRESS. I WILL ANSWER QUESTIONS AND FIELD PROBLEMS AFTER THIS NOTICE HAS DISAPPEARED. An announcement will be made on https://community.openhab.org in due course.

# OHR
OpenHAB Reporter

## Current Status
Beta.

## What Is It?
OHR is a TLA and stands for "OpenHAB Reporter". [OpenHAB](https://openhab.org) is "a vendor and technology agnostic open source automation software for your home". OHR is an aid to documenting an OpenHAB installation. OHR was born after I realised that I should provide adequate documentation for my own smart home for the future when someone else takes it over.

OHR reads OpenHAB's JSON Database and – currently – produces a report on the bindings therein, ordered by thing, bridge, model and binding. It also creates SVG diagrams with the same information for those who prefer pictures.

OHR stores all this information in a file tree that fits perfectly into a [Dokuwiki](https://dokuwiki.org) installation. The generated text and diagrams contain hyperlinks that also work nicely with Dokuwiki.

OHR reports are generated by [Freemarker](https://freemarker.apache.org) using customisable templates. The SVG diagrams are generated using [nidi3/graphviz-java](https://github.com/nidi3/graphviz-java) and [GraphViz](https://graphviz.org) and can also be customised to a certain extent.

OHR creates Dokuwiki text files, some of which also contain SVG data, in a hierarchical file tree that has the following structure:

```
<link prefix>
  bindings
    <binding name 1>
      auto-svg.txt
      auto.txt
      start.txt
      models
        <model name>
          start.txt
          auto.txt
      bridges
        <bridge name>
          start.txt
          auto.txt
      things
        <bridge name>
          start.txt
          auto.txt
    <binding name 1>
    ...
    <binding name N>
```
The "auto" files (those whose names start with "auto", i.e. `auto-svg.txt`, `auto.txt`) are (re)generated each time OHR is run, so previous versions are overwritten. OHR only creates "start" files (`start.txt`) if there is not already one there, so users can modify start files to their heart's content, knowing that the content will not be overwritten (unless something goes dreadfully wrong, of course, but you've made a backup anyway, haven't you?). Users are also free to add any further files to the file tree that are needed in their environment. Thus the documentation of the smart home system can be documented in the normal wiki fashion but with a snapshot of the actual OpenHAB system included and linkable.

## Usage
![OHR Main Windows](https://github.com/winnall/ohr/blob/master/Documentation/Images/Main%20Window.png)

## Sample Report Pages

### Report Home Page
When OHR is started, the following window is displayed.
![Report Home Page](https://github.com/winnall/ohr/blob/master/Documentation/Images/Report%20Home%20Page.png)

### Binding Report
![Binding Report](https://github.com/winnall/ohr/blob/master/Documentation/Images/Binding%20Report.png)

### Thing Report
![Thing Report](https://github.com/winnall/ohr/blob/master/Documentation/Images/Thing%20Report.png)


## Supported Platforms
OHR is designed to work on MacOS, Linux, Unix, Windows and iOS. However, each of these platforms has slightly different conventions, for which a platform-specific class in OHR has to be modified. This is trivial for people with knowledge of the platforms. A little love for the platform is also necessary to ensure that all the application is well-behaved on that platform (i.e. it observes all the platform's conventions: look, feel and general good manners).

The only currently implemented platform is MacOS. Volunteers with a love for the other platforms will be supported as far as possible. More details can be found in the wiki at ...

## Further Information
Please see the [OHR wiki on Github](https://github.com/winnall/ohr/wiki) for further information, background and plans for further development.

## Trivia
