# OHR
OpenHAB Reporter

## Current Status
Beta.

## What Is It?
OHR is a TLA and stands for "OpenHAB Reporter". [OpenHAB](https://openhab.org) is "a vendor and technology agnostic open source automation software for your home". OHR is an aid to documenting an OpenHAB installation. OHR was born after I realised that I should provide adequate documentation for my own smart home for the future when someone else takes it over.

OHR reads OpenHAB's JSON Database and – currently – produces a report on the bindings therein, ordered by thing, bridge, model and binding. It also creates SVG diagrams with the same information for those who prefer pictures.

OHR stores all this information in a file tree that fits perfectly into a [Dokuwiki](https://dokuwiki.org) installation. The generated text and diagrams contain hyperlinks that also work nicely with Dokuwiki.

## Usage
![OHR Main Windows](https://github.com/winnall/ohr/blob/master/Documentation/Images/Main%20Window.png)

## Sample Report Pages

### Report Home Page
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
