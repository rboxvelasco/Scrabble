Set objShell = CreateObject("WScript.Shell")
Set objFSO = CreateObject("Scripting.FileSystemObject")

' Obtener la ruta del script actual
scriptPath = objFSO.GetParentFolderName(WScript.ScriptFullName)

' Construir la ruta completa al archivo .bat
batFile = Chr(34) & scriptPath & "\scrabble.bat" & Chr(34)

' Ejecutar el archivo .bat (0=oculto, True=esperar a que termine)
objShell.Run batFile, 0, True
