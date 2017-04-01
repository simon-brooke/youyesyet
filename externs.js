/* I'm not certain that this file is still needed at all; however, it doesn't seem to be doing any harm. */
/* Things which should not get renamed when compiling ClojureScript */

/* this block relates to the use of Leaflet */
var L = {
  "map": {
    "setView": function(){},
    "eg": function(){}
  },
  "tileLayer": {
    "addTo": function(){}
  }
};

