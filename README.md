# Android Actionbar Search Example and ListView for Beginners

This is a very simple Android example for the search function of the ActionBar.

# Suche mit der Actionbar

Ein simples Beispiel, wie man die Suche von der Actionbar `android.support.v7.widget.SearchView` nutzen kann.
Außerdem ist es auch ein Beispiel, wie man mit einem BaseAdapter Daten und View miteinander zu einem
View `layout/feed_line.xml` zusammenbauen kann.

## Manifest

Weil die `MainActivity` mit einem `AsyncTask` aus dem Internet RSS-Feeds aus
Beispieldaten holt, ist die *Permission* für das Internet gesetzt.

## onCreateOptionsMenu()

Der spanende Teil geschieht in der `onCreateOptionsMenu()`. Ein Listener reagiert, wenn man
die Suche beginnt `OnQueryTextListener()`, ein anderer, wenn man die Suche aufruft bzw. beendet.

## ListView und BaseAdapter

Für das Starten der Suche bzw. für das Ergebnis und beenden der Suche wird einfach nur
mit `setAdapter()` der Adapter des ListView ausgetauscht. Die Suche selbst findet im `BaseAdapter`
statt, da ich darin auch die Daten habe. Entsprechend eines neuen `Queries` werden
im Adapter bei mir die Daten neu zusammengestellt.
