# -*- coding: utf-8 -*-
"""
Created on Thu Sep 28 16:17:14 2017

@author: sach
"""

import sqlite3
sqlite3.version_info
database = sqlite3.connect("batteryMonitor.db")
cur = database.cursor()
cur.execute('SELECT * FROM cells')
data = cur.fetchall()
counter = 0
cell1Voltages =[]
for i in data:
    if (i[1]==1):
        counter +=1
        cell1Voltages.append(i[2])
for i in range(0,len(cell1Voltages))
        print cell1Voltages[i]
        


