# ETCS Test Application

Aplikace pro zaznamenávání a správu testů Evropského vlakového zabezpečovacího systému (ETCS) na mobilních zařízeních Android.

## Popis

ETCS Test Application je mobilní aplikace, která umožňuje železničním technikům zaznamenávat výsledky testů ETCS systému při průjezdu vlaku různými stanicemi. Aplikace poskytuje přehledný způsob, jak dokumentovat testy, přidávat fotografie a exportovat výsledky.

## Hlavní funkce

- **Správa jízd a stanic**: Aplikace umožňuje vytvářet jízdy s předdefinovaným pořadím stanic a časy průjezdu
- **Výběr ze seznamu českých stanic**: Možnost výběru z předem definovaného seznamu železničních stanic
- **Předem definované testy**: Aplikace obsahuje sadu předem definovaných ETCS testů
- **Výsledky testů**: Možnost zaznamenat, zda test prošel, neprošel, nebo byl přesunut na jiný čas
- **Časové záznamy**: Přesné zaznamenání času provedení testu s možností úpravy
- **Fotografická dokumentace**: Ke každému testu lze přidat fotografii
- **Export výsledků**: Možnost exportu výsledků do PDF formátu pro archivaci a reporting

## Technologie

- Java
- Android SDK
- Room Database
- Material Design Components
- iText PDF Library

## Návod k sestavení

1. **Požadavky:**
   - Android Studio (verze 4.0 nebo novější)
   - JDK 11 nebo novější
   - Android SDK (API level 35)

2. **Stažení projektu:**
   ```
   git clone https://github.com/vaseusernamehre/etcs-test-app.git
   cd etcs-test-app
   ```

3. **Sestavení aplikace:**
   - Otevřete projekt v Android Studiu
   - Nechte Gradle synchronizovat projekt
   - Klikněte na "Build" > "Build Bundle(s) / APK(s)" > "Build APK(s)"
   - Nebo spusťte příkaz: `./gradlew assembleDebug`

4. **Instalace aplikace:**
   - Připojte Android zařízení nebo spusťte emulátor
   - Nainstalujte sestavený APK soubor (najdete ho v app/build/outputs/apk/debug/app-debug.apk)
   - Nebo přímo spusťte z Android Studia kliknutím na "Run" > "Run 'app'"

## Základní použití

1. **Vytvoření nové jízdy:**
   - Klikněte na tlačítko "+" na hlavní obrazovce
   - Zadejte číslo vlaku a datum
   - Přidejte stanice a plánované časy příjezdu
   - Uložte jízdu

2. **Přidání testů ke stanici:**
   - Vyberte jízdu ze seznamu
   - Klikněte na stanici
   - Přidejte test kliknutím na tlačítko "+"
   - Vyberte typ testu ze seznamu

3. **Záznam výsledku testu:**
   - Vyberte výsledek (Prošel/Neprošel/Přesunout)
   - Přidejte aktuální čas nebo upravte čas testu
   - Volitelně pořiďte fotografii
   - Uložte výsledek

4. **Export výsledků:**
   - Na obrazovce s detailem jízdy klikněte na ikonu exportu
   - Vyberte umístění pro uložení PDF souboru
