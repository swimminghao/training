import pandas as pd
import numpy as np

refs=[['p1','p2'],['p1','p3'],['p1','p4'],['p2','p7'],['p3','p7'],['p6','p1'],['p3','p2'],['p5','p2']]
auts=[['a1','p1'],['a2','p1'],['a2','p3'],['a1','p7'],['a3','p5'],['a4','p4'],['a2','p4'],['a3','p6'],['a5','p2']]

aut_matrix=pd.DataFrame()
aut_matrix.index.name='author'
author=[]
paper=[]
for aut in auts:
    if aut[1] not in paper:
        aut_matrix[aut[1]]=0
#         aut_matrix.loc[aut[0]]=0
        paper.append(aut[1])
    if aut[0] not in author:
            aut_matrix.loc[aut[0]]=0
#             aut_matrix[aut[1]]=0
            author.append(aut[0])
    aut_matrix.loc[aut[0],aut[1]]=1
# print(aut_matrix)

refed=[]
dicts={}
for ref in refs:
    refed.append(ref[1])
    dicts[ref[1]]=dicts.get(ref[1],0)+1
# print(dicts)
aut_ref=aut_matrix
for index,row in aut_matrix.iterrows():
    for i,item in row.items():
#         print(i,item)
        if item==1:
            if i in dicts.keys():
                aut_ref.loc[index,i]=dicts.get(i)
            else:
                aut_ref.loc[index, i] = 0
print(aut_ref)
for index,row in aut_ref.iterrows():
    row_sorted=row.sort_values(ascending=False)
    k=1
    hi=0
    for num in row_sorted:
        if num<k:
            break
        k=k+1
    hi=k-1
    print(index,hi)