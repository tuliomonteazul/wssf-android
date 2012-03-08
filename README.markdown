WSSF (Web Service Selection Framework)

Projeto criado no trabalho de conclusão de curso para graduação em Ciência da Computação.
Consiste em adaptar o framework WSSF para a plataforma Android com o objetivo de realizar experimentos e testar a execução das políticas de seleção de servidores.


# Requisitos:
 - Certifique-se que o seu dispositivo está com a opção Configurações > Aplicativos > Fontes Desconhecidas marcada, para permitir instalar aplicativos que não são do Market.
 - É necessário que o dispositivo tenha um cartão SD, pois será criada uma pasta para armazenar os arquivos de configuração.

# Instalação

Para executá-lo, basta instalar o arquivo wssf-android.apk. Essa instalação pode ser feita através de comandos adb (Android Debug Bridge) ou de gerenciadores de arquivos para o Android.
Um dos mais utilizados é o Astro File Manager. Para instalar com ele, basta ter o wssf-android.apk no seu cartão SD e, abrindo o arquivo com o Astro, selecionar a opção Instalar.
Outro gerenciador que também pode instalar é o Dropbox.

Esse link mostra detalhadamente 3 maneiras de instalar um apk:
http://maketecheasier.com/install-applications-without-the-market/2011/01/28

# Como funciona

O aplicativo possui 2 tipos de execução: simples e múltipla. Para ambos é necessário ter o arquivo replicas.txt no diretório /mnt/sdcard/WSSF/. Para facilitar o uso, esse diretório será criado automaticamente pela aplicação, assim como um arquivo de réplicas padrão com o seguinte conteúdo:

http://mozilla.mirror.rafal.ca/addons/71/enigmail-0.95.1-tb+sm.xpi
http://gd.tuwien.ac.at/infosys/browsers/mozilla.org/addons/71/enigmail-0.95.1-tb+sm.xpi
http://mozilla.phphosts.org/addons/71/enigmail-0.95.1-tb+sm.xpi
http://mozilla.saix.net/addons/71/enigmail-0.95.1-tb+sm.xpi
http://pv-mirror02.mozilla.org/pub/mozilla.org/addons/71/enigmail-0.95.1-tb+sm.xpi

Esse arquivo padrão possui réplicas de um arquivo de 1 mb.

## Execução Simples
 Exibirá o progresso de todas as invocações e guardará o resultado no arquivo /mnt/sdcard/WSSF/experiments.txt.

## Execução Múltipla
 Invoca sequencialmente várias execuções, determinadas a partir de um arquivo de configuração, e guarda os resultados no arquivo /mnt/sdcard/WSSF/experiments.txt.
 O arquivo de configuração deverá possuir o nome execution-setup e estar guardado na pasta /mnt/sdcard/WSSF/, para definir quais serão as execuções que deverão ser realizadas e qual a quantidade de repetições.
 
 Segue um exemplo desse arquivo:

repeat 5
R1 NO
R1 P
R1 FC
R1 FR
R1 BP[20,0.5]
R1 BP[20,1]
R1 BP[20,1.5]

