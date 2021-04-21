# WEKA Proiektua
Helburua datu multzo batean datu meatzaritza aplikatzea da. Datu multzoan Tweet desberdinak dauzkagu eta atazaren zeregina Tweet horiek gorroto sentimenduak dituzten Tweetak sailkatzea da. Sinpleagoa izateko, gorroto sentimenduak kutsu arrazista zein sexista dituzten Tweetak direla kontsideratuko da. 

Sailkapena egiteko bi eredu iragarle desberdin erabiliko dira, Logistic Regression, baseline bezala erabilko dena, eta Multilayer Perceptron. Bi ereduen jarduera konparatuko da, eta lortutako emaitzen analisia garatuko da dokumentazioan.

## Aplikazioaren paketeak
Aplikazioa hiru atal nasusitan banatuta dago.

### Aurre-prozesamendua
1. GetRaw: Datuen dokumentua ad-hoc ARFF formatura bihurtzen du programa honek. Honen bitartez,train zein test datuei formatu egokia ematen die, beharrezkoak ez diren atributuak kenduz etatestuari arazoak eman ditzaketen karaktereak kenduz.
2. TransformRaw: Datu gordinak erabiltzaileak nahi duen espazio bektorialera aldatzeko eta espazio horren hiztegia lortzeko pentsatuta dago programa hau.
3. MakeCompatible: Datu gordinak lortu eta gero eta atributu hautapena egin ondoren, test multzoa espazio honetara egokitzeaz arduratzen da.

### Sailkatzailearen inferentzia
1. FSS:  Entrenamendu  multzoko  atributu  egokienak  hautatu  behar  dira,  atributu  gehiegi  izatea kaltegarria izan daitekelako.  Alde batetik, doikuntza eza edo underfitting eta bestetik bariantza altuak eta sesgoak estatistiketan eman daitezke. Hori dela eta, informazio galera txikiena emango lukeen atributuen hautapena gauzatzen da. Horretaz gain, test multzoa ere egokitzen da.
2. GetBaselineModel: Logistic Regression erabiliz markatu da behe-bornea, kalitatea estimatzeko hiru ebaluazio metodo erabili dira: Ez-zintzoa, 10-fold cross validation eta 100 partiketa ezberdinekin egindako hold-out baten batazbesteko erantzunak.
3. ParamOptimization: Parametro ekorketa teknika erabiliz, esleitutako algoritmoarentzako parametro optimoenak kalkulatzen eta gordetzen dira.
4. GetModel: Kalkulatutako  parametroekin  esleitutako  algoritmoaren  modeloa  eraikitzen  da,  eta honen kalitatearen ebaluazio bat egiten da.  Kalitatea estimatzeko hiru ebaluazio metodo erabili dira: Ez-zintzoa, 10-fold cross validation eta 100 partiketa ezberdinekin egindako hold-out baten batazbesteko erantzunak.

### Iragarpenak
1.Predictions: Test multzoa emanda, emandako eredu iragarleak egindako iragarpenak gordetzen ditu fitxategi batean. Gainera, garatutako interfaze grafikoan testu soila sartzeko aukera ere emanten da, test fitxategiaz aparte, instantzia bakarraren iragarpena egiteko.

## Interfaze Grafikoaren erabilera
Erabiltzaileari erraztasun gehiago emateko helburuarekin interfaze grafiko bat garatu da ataza batzuk gauzatzeko. Interfazeak hiru erlaitz ditu:
1. Preprocess: Datuak .csv formatuan emanda, horiek formateatzen eta egokitzen ditu.
  - Sartu beharreko fitxategiak: Train eta test multzo gordinak .csv formatuan.
  - Sartu beharreko parametroak: Atributu espazioaren zein errepresentazio nahi den, BoW/TF·IDF, eta datuen formatua, Sparse/NonSparse.
2. FSS: Train eta test multzoen atributuen hautapena egiten da, informazio irabaziaren irizpidea erabiliz. Gainera, test multzoaren egokitzapena egiten da ere. OHARRA: Aldez aurretik hiztegia sortuta izan behar da exektutatu baino lehen, hau da, preprocess aldez aurretik eginda egon behar da.
  - Sartu beharreko fitxategiak: Train multzoa .arff formatuan eta atributu espazioa aplikatuta eta test multzoa .arff formatuan.
  - Sartu beharreko parametroak: Train multzoaren atributu espazioaren xehetasunak, BoW/TF·IDF eta Sparse/NonSparse alegia.
3. Predictions: Iragarpenak egiten dira erlaitz honetan eta horretarako bi aukera ematen dira. Alde batetik, test multzoa emanda instantzia multzo baten iragarpenak egitea, eta bestetik testu soila sartuz Tweet horren sentimenduen iragarpena egitea. 
  - Derrigorrez sartu beharreko fitxategiak: iragarpenak gordeko diren fitxategiaren helbideaeta eredu iragarlearen fitxategiaren helbidea. 
  - Test multzoaren iragarpenak egiteko fitxategiak: Train multzoaren formatu berdina duen fitxategiaren helbidea.
  - Idatzitako testuaren iragarpenak egiteko fitxategiak: Atributu guztiak dituen entrenamendu multzoaren helbidea formatu egokian, eta erabili den formatua (BoW/TD·IDF, Sparse/NonSparse).
 
OHAR GARRANTZITSUA: Aplikazioak erlaitz bakoitzeko programak exekutatzen dituen bitartean programa blokeatuta geldituko da.

## Esteka interesgarriak
[Hasierako datu sorta, lortutako emaitzekin](https://github.com/EmmaManna/WEKA_Proiektua/blob/master/Datasets%26%26Outputs.zip) eta [exekutagarriak](https://github.com/EmmaManna/WEKA_Proiektua/blob/master/Exekutagarriak.zip) hemen aurkitu daitezke.

## Developed by
[![Website](https://img.shields.io/badge/XabiDermit-github-green?style=flat-square)](https://github.com/XabiDermit)
[![Website](https://img.shields.io/badge/EmmaManna-github-green?style=flat-square)](https://github.com/EmmaManna)
[![Website](https://img.shields.io/badge/JonGondra-github-green?style=flat-square)](https://github.com/JonGondra)
