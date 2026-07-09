#Пункт 1
#Создадм дерево согласно заданию
mkdir -p lab0/{bulbasaur0,glalie2/{cleffa,stunky},omastar0/{frillish,cascoon,mamoswine}}
touch lab0/bellossom5  lab0/swoobat2 lab0/bulbasaur0/{blissey,leavanny,numel,spoink} lab0/glalie2/slowpoke lab0/omastar0/mantyke
# Заполнняем файлы информацией
echo "Ходы Absorb After You Body Slam Bullet Seed Double-Edge
Gastro Acid Giga Drain Drain Punch Leaf Blade≠ Mega Drain Natural
Gift Petal Dance Secret Power Seed Bomb Sleep Talk Snore Stun Spore≠
Sunny Day≠ Sweet Scent≠ Synthesis Uproar Worry Seed" > lab0/bellossom5
echo "Ходы
Avalanche Block Body Slam Covet Drain Punch Dynamicpunch Endeavor Fire
Punch Focus Punch Gravity Heal Bell Helping Hand Hyper Voice Ice Punch
Icy Wind Iron Tail Last Resort Mega Kick Mega Punch Mimic Mud-Slap
Recycle Rollout Seismic Toss Shock Wave Skill Swap Snatch Snore
Stealth Rock Thunderpunch Uproar Water Pulse Zen
Headbutt" > lab0/bulbasaur0/blissey
echo "Тип покемона BUG GRASS" > lab0/bulbasaur0/leavanny
echo "weigth=52.9
height=28.0 atk=6 def=4" > lab0/bulbasaur0/numel
echo -e "Развитые способности\nGlutto
ny" > lab0/bulbasaur0/spoink
echo "Живет Beach Freshwater" > lab0/glalie2/slowpoke
echo "satk=6 sdef=12
spd=5" > lab0/omastar0/mantyke
echo "Развитые способности Simple" > lab0/swoobat2
echo "satk=4 sdef=6
spd=5" > lab0/venonat6

#Пункт 2
#Задаём права для файлов
chmod u=rw,g=,o= lab0/bellossom5
chmod 736 lab0/bulbasaur0
chmod 660 lab0/bulbasaur0/blissey
chmod 440 lab0/bulbasaur0/leavanny
chmod 604 lab0/bulbasaur0/numel
chmod 404 lab0/bulbasaur0/spoink
chmod 573 lab0/glalie2
chmod 513 lab0/glalie2/cleffa
chmod 444 lab0/glalie2/slowpoke
chmod 733 lab0/glalie2/stunky
chmod 736 lab0/omastar0
chmod 317 lab0/omastar0/frillish
chmod 764 lab0/omastar0/cascoon
chmod 335 lab0/omastar0/mamoswine
chmod 444 lab0/omastar0/mantyke
chmod u-rwx,g+rw,o-rwx lab0/swoobat2
chmod 404 lab0/venonat6

#Пункт 3.
cp -R lab0/glalie2 lab0/omastar0/cascoon/
cat lab0/bulbasaur0/numel > lab0/swoobat2_73
ln -s lab0/bulbasaur0 lab0/Copy_75
cat lab0/bellossom5 > lab0/bulbasaur0/leavannybellossom
cp lab0/venonat6 lab0/omastar0/cascoon/
ln lab0/swoobat2 lab0/bulbasaur0/blisseyswoobat
ln -s lab0/venonat6 lab0/glalie2/slowpokevenonat
# Вылезла ошибка - ln: lab0/glalie2/slowpokevenonat: Permission denied
# Выдадим права и создадим ссылку заново
chmod 700 lab0/glalie2
ln -s lab0/venonat6 lab0/glalie2/slowpokevenonat
chmod 573 lab0/glalie2

#Пункт 4.
wc -m lab0/swoobat2 2> /tmp/err502026
ls -Rp lab0/glalie2/ | grep -v '/$'
grep -rn "ny" lab0/glalie2 2>&1
wc -l lab0/bellossom5 > /tmp/CountLines 2>&1
ls -Rul lab0/bulbasaur0/ | sort -k8 2>&1 #сортировка по времени последнего доступа
cat -n lab0/bellossom5 | grep -v 'e$' 2>&1

 #Пункт 5.
rm -f swoobat2
rm -f lab0/bulbasaur0/spoink
rm -f lab0/glalie2/slowpokevenonat
#Получили ошибку - rm: lab0/glalie2/slowpokevenonat: Недостаточно привилегий
# Меняем права и удаляем файл
chmod 700 lab0/glalie2
rm -f lab0/glalie2/slowpokevenonat
#Возвращаем права обратно
chmod 573 lab0/glalie2
rm -f lab0/bulbasaur0/blisseyswoobat
rm -Rf lab0/omastar0
#Получили много ошибок
# rm: lab0/omastar0/frillish: Недостаточно привилегий
# rm: lab0/omastar0/cascoon/glalie2/stunky: Недостаточно привилегий
# rm: lab0/omastar0/cascoon/glalie2/slowpoke: Недостаточно привилегий
# rm: lab0/omastar0/cascoon/glalie2/cleffa: Недостаточно привилегий
# rm: lab0/omastar0/cascoon/glalie2: Каталог не пуст
# rm: lab0/omastar0/cascoon: Каталог не пуст
# rm: lab0/omastar0/mamoswine: Недостаточно привилегий
# rm: lab0/omastar0: Каталог не пуст
chmod -R 700 lab0/omastar0
rm -Rf lab0/omastar0
chmod 700 lab0/glalie2
rm -Rf lab0/glalie2/stunky
chmod 573 lab0/glalie2