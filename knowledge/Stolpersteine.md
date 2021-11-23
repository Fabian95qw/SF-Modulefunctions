Work in Progress.

Dateien im /var/starface/module/instances/repo/[UUID]/res werden beim Speichern/Übernehmen der Instanz gelöscht, wenn diese nicht via "Resources" hochgeladen wurden.
Dieser Ort sollte deshalb nicht zur Ablage von Dateien verwendet werden!

Default-Variablen für Felder können nach dem Update der STARFACE einen null Wert enthalten, bis dieser geändert wird. Diese Art vo Fehler muss im Modul abgefangen werden.
Beispiel: Ein Dropdown enthält 3 Werte. "A", "B", "C". Der Standartwert ist auf "A" gesetzt. Es gibt ein Switch Case mit diesen 3 Optionen.
Vor dem Update, enthielt der GUI_DROPDOWN den Wert "A", nach dem Update der STARFACE steht "null" in diesem Wert. Der Switch-Case kennt diese Option nicht, und dementsprechend wird keine der 3 Optionen ausgeführt.

