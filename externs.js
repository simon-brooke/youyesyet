/* Things which should not get renamed when compiling ClojureScript */

/* this block relates to the use of Leaflet */
var L = {
    "map": {
    "setView": function(){}
    },
    "tileLayer": {
    "addTo": function(){}
    }
};
