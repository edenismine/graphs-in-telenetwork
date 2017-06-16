from random import randint
from xml.etree.ElementTree import Element as Elem
from xml.etree.ElementTree import ElementTree as Tree

F_NAMES = """Luis
Daniel
Carlos
Manuel
Kai
Jorge
Liz
Ignacio
Maria
Miguel
Jose
Antonio
Alejandro
Patricia
Josefina
Rosa
Alicia
Francisco
Margarita
Alejandra
Elizabeth""".splitlines()

L_NAMES = """Rodriguez
Hernandez
Garcia
Martinez
Gonzalez
Lopez
Perez
Sanchez
Kawasaki
Aragon
Bermudez
Ueda
Ramirez
Flores
Arguello""".splitlines()

STATIONS = """55-Mexico City
81-Monterrey, Nuevo Leon
33-Guadalajara, Jalisco
656-Ciudad Juarez, Chihuahua
614-Chihuahua, Chihuahua
999-Merida, Yucatan
222-Puebla, Puebla
442-Queretaro, Queretaro
449-Aguascalientes, Aguascalientes
664-Tijuana, Baja California
844-Saltillo, Coahuila
686-Mexicali, Baja California
667-Culiacan, Sinaloa
722-Toluca, Mexico
998-Cancun, Quintana Roo
871-Torreon, Coahuila
744-Acapulco, Guerrero
444-San Luis Potosi, San Luis Potosi
833-Tampico, Tamaulipas
477-Leon, Guanjuato
961-Tuxtla Gutierrez, Chiapas
662-Hermosillo, Sonora
229-Veracruz, Veracruz
443-Morelia, Michoacan
951-Oaxaca, Oaxaca""".splitlines()


def main():
    root = Elem('Network')
    area_codes = []
    for station in STATIONS:
        words = station.split('-')
        area_code = words[0]
        area_codes.append(area_code)
        name = words[1]
        new_station = Elem('Station')
        new_station.set('code', str(area_code))
        new_station.set('name', name)
        appendclients(new_station, randint(1, 5))
        root.append(new_station)

    min_conn = ((len(area_codes) - 1) * (len(area_codes) - 2)) // 2 + 1
    links = set()
    for _ in range(min_conn):
        station_a, station_b = getlink(links, area_codes)
        new_link = Elem('Link')
        new_link.set('stationACode', station_a)
        new_link.set('stationBCode', station_b)
        root.append(new_link)

    root.set('links', str(min_conn))
    root.set('stations', str(len(area_codes)))
    with open('network.xml', 'wb') as f:
        f.write(b'<?xml version="1.0" encoding="UTF-8" ?><!DOCTYPE Network SYSTEM "Network.dtd">')
        Tree(root).write(f, 'utf-8')


def appendclients(station, clients):
    for _ in range(clients):
        new_client = Elem('Client')
        new_client.set('name', getname())
        new_client.set('phone', getphone())
        station.append(new_client)


def getlink(links, area_codes):
    station_a = station_b = 0
    while station_a == station_b \
            or (station_a, station_b) in links \
            or (station_b, station_a) in links:
        station_a = area_codes[randint(0, len(area_codes) - 1)]
        station_b = area_codes[randint(0, len(area_codes) - 1)]
    links.add((station_a, station_b))
    return str(station_a), str(station_b)


def getname():
    f_name = F_NAMES[randint(0, len(F_NAMES) - 1)]
    l_name = L_NAMES[randint(0, len(L_NAMES) - 1)]
    return f'{f_name} {l_name}'


def getphone():
    phone = [str(randint(1, 9)) for _ in range(8)]
    return ''.join(phone)


if __name__ == '__main__':
    main()
