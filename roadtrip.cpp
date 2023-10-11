// 2021 UCF HSPT - Alphabetic Road Trip
// Author: Sathvik Kuthuru

#include <bits/stdc++.h> 
using namespace std; 

#define INF 100000000000000L

typedef long long ll;
typedef pair<ll, int> pi; 
typedef pair<pi, int> pii;


// We can use dijkstra's algorithm to find the shortest path for the trip

void solve() {
    int n, m;
    cin >> n >> m;
    // Input of cities
    vector<string> a;
    // Adjacency list to store connects between cities
    vector<pi> adj[n];
    // Map to get the index of a city
    map<string, int> getIndex;

    for(int i = 0; i < n; i++) {
        string curr;
        cin >> curr;
        a.push_back(curr);
        getIndex.insert(make_pair(curr, i));
    }
    // Add a bidirectional edge between the two cities with edge weight w
    for(int i = 0; i < m; i++) {
        string f, s;
        cin >> f >> s;
        int u = getIndex[f], v = getIndex[s];
        int w;
        cin >> w;
        adj[u].push_back(make_pair(w, v));
        adj[v].push_back(make_pair(w, u));
    }
    // Priority Queue for dijkstra's algorithm
    // Keeps track of the current distance, how many letters have been finished, and the current city index
    priority_queue<pii, vector<pii>, greater<pii>> pq;
    // We want to go until all letters A through J have been finished 
    int len = (int) ('J' - 'A') + 2;
    vector<ll> dist[len];
    // Initially set the shortest distances to INF
    for(int i = 0; i < len; i++) {
        for(int j = 0; j < n; j++) dist[i].push_back(INF);
    }
    // We can start at any 'A' city with distance 0
    for(int i = 0; i < n; i++) {
        if(a[i][0] == 'A') {
            pq.push(make_pair(make_pair(0, i), 1));
            dist[1][i] = 0;
        }
    }
    // Keep running while priority queue is not empty
    while(!pq.empty()) {
        // Get the vertex with the least distance
        pii curr = pq.top();
        pq.pop();
        int at = curr.first.second, have = curr.second;
        if(have == len - 1) continue;
        // Standard greedy way of relaxing the distance to adjacent vertices
        // Important to notice that here the number of finished letters doesn't change
        for(int i = 0; i < adj[at].size(); i++) {
            ll w = adj[at][i].first;
            int to = adj[at][i].second;
            if(dist[have][at] + w < dist[have][to]) {
                dist[have][to] = dist[have][at] + w;
                pq.push(make_pair(make_pair(dist[have][to], to), have));
            }
        }
        // If we are at a city with the current letter we are looking for to finish in our roadtrip
        // then we can increase the number of letters we've finished with no additional cost
        int currLetter = (int) (a[at][0] - 'A');
        if(currLetter == have && dist[have][at] < dist[have + 1][at]) {
            dist[have + 1][at] = dist[have][at];
            pq.push(make_pair(make_pair(dist[have + 1][at], at), have + 1));
        }
    }
    // Find the minimum cost ending to the roadtrip
    ll ans = INF;
    for(int i = 0; i < n; i++) {
        ans = min(ans, dist[len - 1][i]);
    }
    // Print out answer
    cout << ans << '\n';
}

int main() 
{
    // Scan in number of test cases to process
    int numTests;
    cin >> numTests;
    for(int i = 1; i <= numTests; i++) {
        solve();
    }
    return 0; 
} 
