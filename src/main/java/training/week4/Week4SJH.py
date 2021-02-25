import pandas as pd

miss=[['A','B'],['A','C'],['B','D'],['D','E'],['D','F'],['C','E'],['C','G'],['E','G'],['F','G'],['H','J']]
df=pd.DataFrame()
df.index.name='key'
key=[]
for mis in miss:
    if mis[0] not in key:
        df[mis[0]]=0
        df.loc[mis[0]]=0
        key.extend(mis[0])
    if mis[1] not in key:
            df.loc[mis[1]]=0
            df[mis[1]]=0
            key.extend(mis[1])
    df.loc[mis[0],mis[1]]=1
print(df)
ords=[]
while df.empty is False:
    order=df.loc[:,((df==0).all())].columns.tolist()
    ords.append(order)
    df=df.drop(columns=order)
    df=df.drop(index=order)
    print(order)
print(ords)