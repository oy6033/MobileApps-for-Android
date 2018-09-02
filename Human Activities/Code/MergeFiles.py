ext = '.txt'
#set to your working directory
#set to the name of your output file
results = 'C:/Users/Rishi/Documents/Degree/Summer 18/CSE535 Mobile Computing/Project/all_backup/'
import os
import sys

def after(value, a):
    # Find and validate first part.
    pos_a = value.rfind(a)
    if pos_a == -1: return ""
    # Returns chars after the found string.
    adjusted_pos_a = pos_a + len(a)
    if adjusted_pos_a >= len(value): return ""
    return value[adjusted_pos_a:]

def merge_files(file_type,trainotest):
    if trainotest=="test_x":
        dir_path="test/"
    else:
        dir_path="train/"
    files = os.listdir(results+file_type+dir_path)
    for f in files:
      if f.endswith(ext) or f.endswith("csv") :
        data = open(results+file_type+dir_path+f)
        out = open(results+file_type+trainotest+"_1.txt", 'a')
        for l in data:
            if file_type=="Eating/":
                print(l[14:].replace(" ",""), file=out)
            else:
                print(l.replace(";",","), file=out)
        data.close()
        out.close()


    f2=open(results+file_type+trainotest+".txt","+w")
    with open(results+file_type+trainotest+"_1.txt") as f:
        for line in f:
            if not line.isspace():
                f2.write(line)

    os.remove(results+file_type+trainotest+"_1.txt")


merge_files("Eating/","test_x")
merge_files("Eating/","train_x")
