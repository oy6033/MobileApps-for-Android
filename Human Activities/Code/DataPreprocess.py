# -*- coding: utf-8 -*-
"""
Created on Sun Jul 29 02:15:16 2018

@author: Rishi
"""
from tempfile import TemporaryFile
from numpy import genfromtxt
import numpy as np
f3=open("C:/Users/Rishi/Documents/Degree/Summer 18/CSE535 Mobile Computing/Project/Code/data/y.csv","+w")
f4=open("C:/Users/Rishi/Documents/Degree/Summer 18/CSE535 Mobile Computing/Project/Code/data/x.csv","+w")
path_1='C:/Users/Rishi/Documents/Degree/Summer 18/CSE535 Mobile Computing/Project/Code/data/'
def format(filename):
    f1=open("C:/Users/Rishi/Documents/Degree/Summer 18/CSE535 Mobile Computing/Project/Code/data/y.csv","+a")
    f2=open("C:/Users/Rishi/Documents/Degree/Summer 18/CSE535 Mobile Computing/Project/Code/data/x.csv","+a")
    path='C:/Users/Rishi/Documents/Degree/Summer 18/CSE535 Mobile Computing/Project/Code/data/'+filename
    n=64
    load = np.genfromtxt(str(path), delimiter=';',usecols=np.arange(0,8))
    actual=load.size-(load.size%n)
    one_row=np.reshape(load,(1,-1))
    one_row_new=one_row[:1,:actual]
    mul=1
    one_row_new2=np.tile(one_row_new,mul)
    matrix_64=np.reshape(one_row_new2,(-1,n))
    print(actual)
    rows=int(actual/n)*mul
    c=np.asarray(matrix_64)
    np.savetxt(f2,c, delimiter=",")
    if filename==file1:
        yarray=np.array([0,1,0,0])
    elif filename==file2:
        yarray=np.array([1,0,0,0])
    elif filename==file3:
        yarray=np.array([0,0,1,0])
    elif filename==file4:
        yarray=np.array([0,0,0,1])
    new_yarray=np.array([yarray]*rows)
    k=np.asarray(new_yarray)
    np.savetxt(f1,k, delimiter=",")
    f1.close()
    f2.close()

def format_new(trainotest,datacat):
    f1=open("C:/Users/Rishi/Documents/Degree/Summer 18/CSE535 Mobile Computing/Project/all_backup/"+datacat+"/"+trainotest+"_y_formatted.txt","+w")
    f2=open("C:/Users/Rishi/Documents/Degree/Summer 18/CSE535 Mobile Computing/Project/all_backup/"+datacat+"/"+trainotest+"_x_formatted.txt","+w")
    path="C:/Users/Rishi/Documents/Degree/Summer 18/CSE535 Mobile Computing/Project/all_backup/"+datacat+"/"+trainotest + "_x.txt"
    n=64
    load = np.genfromtxt(str(path), delimiter=',')
    actual=load.size-(load.size%n)
    one_row=np.reshape(load,(1,-1))
    one_row_new=one_row[:1,:actual]
    mul=1
    one_row_new2=np.tile(one_row_new,mul)
    matrix_64=np.reshape(one_row_new2,(-1,n))
    print(actual)
    rows=int(actual/n)*mul
    c=np.asarray(matrix_64)
    np.savetxt(f2,c, delimiter=",")
    if datacat=="Eating":
        yarray=np.array([1,0,0,0])
    elif datacat=="Like":
        yarray=np.array([0,1,0,0])
    elif datacat=="Rock":
        yarray=np.array([0,0,1,0])
    elif datacat=="Victory":
        yarray=np.array([0,0,0,1])
    new_yarray=np.array([yarray]*rows)
    k=np.asarray(new_yarray)
    np.savetxt(f1,k, delimiter=",")
    f1.close()
    f2.close()


format_new("train","Eating")
#format_new("train","Non_Eating")
format_new("test","Eating")
#format_new("test","Non_Eating")
format_new("train","Like")
format_new("train","Rock")
format_new("test","Like")
format_new("test","Rock")
format_new("train","Victory")
format_new("test","Victory")

def merge_files(trainotest):

    dir_path1="C:/Users/Rishi/Documents/Degree/Summer 18/CSE535 Mobile Computing/Project/all_backup/"
    filenames = [dir_path1+'Eating/'+trainotest+'_x_formatted.txt',dir_path1+'Like/'+trainotest+'_x_formatted.txt',dir_path1+'Rock/'+trainotest+'_x_formatted.txt',dir_path1+'Victory/'+trainotest+'_x_formatted.txt']
    with open(dir_path1+'Merged/'+trainotest+'/x.csv', '+w') as outfile:
        for fname in filenames:
            with open(fname) as infile:
                for line in infile:
                    outfile.write(line)

    filenames = [dir_path1 + 'Eating/'+trainotest+'_y_formatted.txt',dir_path1 + 'Like/'+trainotest+'_y_formatted.txt',dir_path1 + 'Rock/'+trainotest+'_y_formatted.txt',dir_path1 + 'Victory/'+trainotest+'_y_formatted.txt']
    with open(dir_path1+'Merged/'+trainotest+'/y.csv', '+w') as outfile:
        for fname in filenames:
            with open(fname) as infile:
                for line in infile:
                    outfile.write(line)

    train_set = TemporaryFile()
    y = np.genfromtxt(str(dir_path1 + "Merged/" + trainotest +"/y.csv"), delimiter=',')
    x = np.genfromtxt(str(dir_path1 + "Merged/" + trainotest +"/x.csv"), delimiter=',')
    print(np.shape(y))
    np.savez(dir_path1 + "Merged/" + trainotest + "_set.npz", y=y, x=x)


merge_files("train")
merge_files("test")

f3.close()
f4.close()



