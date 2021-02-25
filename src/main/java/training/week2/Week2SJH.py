a=input('请输入起点：')
b=input('请输入终点：')
inf = float('inf')
matrix_distance = [[0, 3, inf, inf, inf, inf, inf, inf, inf, inf, inf, inf, inf, inf, inf, inf, inf, inf, inf],
                   [3, 0, 4, inf, inf, inf, inf, inf, inf, inf, inf, inf, inf, 3, inf, inf, inf, inf, inf],
                   [inf, 4, 0, 4, inf, inf, inf, inf, inf, inf, inf, inf, inf, inf, inf, inf, inf, inf, inf],
                   [inf, inf, 4, 0, 5, inf, 3, inf, inf, inf, inf, inf, inf, inf, inf, inf, inf, inf, inf],
                   [inf, inf, inf, 5, 0, inf, inf, inf, inf, inf, inf, inf, inf, inf, inf, inf, inf, inf, inf],
                   [inf, inf, inf, inf, inf, 0, 4, inf, inf, inf, inf, inf, inf, inf, inf, inf, inf, inf, inf],
                   [inf, inf, inf, 3, inf, 4, 0, 3, inf, inf, inf, inf, inf, inf, inf, inf, inf, inf, inf],
                   [inf, inf, inf, inf, inf, inf, 3, 0, 3, inf, inf, inf, inf, inf, inf, inf, inf, inf, inf],
                   [inf, inf, inf, inf, inf, inf, inf, 3, 0, 4, inf, inf, inf, inf, inf, 5, inf, inf, inf],
                   [inf, inf, inf, inf, inf, inf, inf, inf, 4, 0, 3, inf, inf, inf, inf, inf, inf, inf, inf],
                   [inf, inf, inf, inf, inf, inf, inf, inf, inf, 3, 0, inf, inf, inf, inf, inf, inf, inf, inf],
                   [inf, inf, inf, inf, inf, inf, inf, inf, inf, inf, inf, 0, 5, inf, inf, inf, inf, inf, inf],
                   [inf, inf, inf, inf, inf, inf, inf, inf, inf, inf, inf, 5, 0, 4, inf, inf, inf, inf, inf],
                   [inf, 3, inf, inf, inf, inf, inf, inf, inf, inf, inf, inf, 4, 0, 3, inf, inf, inf, inf],
                   [inf, inf, inf, inf, inf, inf, inf, inf, inf, inf, inf, inf, inf, 3, 0, 3, inf, inf, inf],
                   [inf, inf, inf, inf, inf, inf, inf, inf, 5, inf, inf, inf, inf, inf, 3, 0, 4, inf, inf],
                   [inf, inf, inf, inf, inf, inf, inf, inf, inf, inf, inf, inf, inf, inf, inf, 4, 0, 4, inf],
                   [inf, inf, inf, inf, inf, inf, inf, inf, inf, inf, inf, inf, inf, inf, inf, inf, 4, 0, 3],
                   [inf, inf, inf, inf, inf, inf, inf, inf, inf, inf, inf, inf, inf, inf, inf, inf, inf, 3, 0]]
distation = ['A1', 'A2', 'A3', 'A4', 'A5', 'B1', 'B2', 'B3', 'B4', 'B5', 'B6', 'C1', 'C2', 'C3', 'C4', 'C5', 'C6', 'C7',
             'C8']

def dijkstra(matrix_distance, source, end, distation):
    source_node = distation.index(source)
    end_node = distation.index(end)
    inf = float('inf')
    # init the source node distance to others
    dis = matrix_distance[source_node]
    node_nums = len(dis)
    #     path=[]
    #     for i in range(0,node_nums):
    #         a=distation[i]
    #         path.append([source,a])
    path = [source_node] * node_nums
    flag = [0 for i in range(node_nums)]
    flag[source_node] = 1
    for i in range(node_nums - 1):
        min = inf
        # find the min node from the source node
        for j in range(node_nums):
            if flag[j] == 0 and dis[j] < min:
                min = dis[j]
                u = j
        flag[u] = 1
        # update the dis
        for v in range(node_nums):
            if flag[v] == 0 and matrix_distance[u][v] < inf:
                if dis[v] > dis[u] + matrix_distance[u][v]:
                    dis[v] = dis[u] + matrix_distance[u][v]
                    path[v] = u
        if u == end_node:
            break
        route = [distation[end_node]]
        finish = end_node
        while finish != source_node:
            finish = path[finish]
            route.append(distation[finish])
        route.reverse()

    return dis[end_node], route
print(dijkstra(matrix_distance, a,b,distation))