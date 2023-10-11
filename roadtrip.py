# Solution to RoadTrip (HSPT 2021 - On Site)
# Author: Atharva Nagarkar

import heapq

# constant for infinity (large value)
oo = 1000000000

# read in test cases
tests = int(input())

for tc in range(1, tests + 1):

    # read in bounds for the problem
    n, m = map(int, input().split())

    # dict to store the node id for the string names
    nameToId = {}

    # the list of nodes for each alphabet 'A' -> 'J' + an extra "sink"
    nameList = [ [] for i in range(11) ]

    # list to store the graph as an adjacency list
    graph = [ [] for i in range(n + 1) ]

    # tables to store the best answer to get to this node
    dp = [ oo for i in range(n + 1) ]
    dist = [ 0 for i in range(n + 1) ]

    # read in the names and assign them ids
    for i in range(n):
        name = input().strip()
        nameToId[name] = i
        c = ord(name[0]) - ord('A')
        
        # add the node to its corresponding first-letter list
        nameList[c].append(i)

        # add each node to the "sink"
        graph[n].append((i, 0))

    # the only node for the "sink" letter is the "sink" node
    nameList[10].append(n)
    
    # read in the edges of the graph
    for i in range(m):
        parts = input().strip().split()

        # get the ids of the given locations
        u = nameToId[parts[0]]
        v = nameToId[parts[1]]

        # read in the weight of the edge
        w = int(parts[2])

        # add it as a bi-directional edge in our graph
        graph[u].append((v, w))
        graph[v].append((u, w))
    
    # function for a modified dijkstra's algorithm
    # this will tell us the shortest distance to reach each s-letter nodes from the t-letter nodes
    def dijk(s, t):

        # initially set the distances of each node to be infinity (not reached yet)
        for i in range(n + 1): dist[i] = oo
        
        # make our priority queue (which is implemented as a heapq)
        q = []
        # push the relevant nodes (t-letter nodes) to our priority queue
        for node in nameList[t]:
            # the weight of the node will be the minimum cost needed to reach here from the sink
            heapq.heappush(q, (dp[node], node))
            dist[node] = dp[node]
        
        # run dijkstra's algorithm
        while len(q) > 0:
            cost, node = heapq.heappop(q)
            if cost > dist[node]: continue
            for v, w in graph[node]:
                if dist[v] > cost + w:
                    dist[v] = cost + w
                    heapq.heappush(q, (dist[v], v))

        # update the answer for the s-letter nodes
        for node in nameList[s]:
            dp[node] = dist[node]
    
    # initially set the cost of reaching the "sink" node to be 0
    dp[n] = 0

    # now for each character from 'J' down to 'A', we have to run the
    # modified dijkstra's algorithm to find the best cost to go in between
    # adjacent letters in the alphabet
    for c in range(9, -1, -1):
        dijk(c, c + 1)
    
    # our answer will be the minimum distance to reach some "A"-letter node
    # from the sink
    ans = oo
    for node in nameList[0]:
        ans = min(ans, dp[node])
    
    # print it!
    print("{ans}".format(ans = ans))
