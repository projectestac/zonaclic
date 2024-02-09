// Self-destroying service worker
// From: https://www.ankursheel.com/blog/programmatically-remove-service-worker
// And: https://stackoverflow.com/questions/45467842/how-to-clear-cache-of-service-worker

self.addEventListener('install', function (e) {
  self.skipWaiting();
});

self.addEventListener('activate', function (e) {

  // Clear caches
  e.waitUntil(
    caches.keys().then(function (cacheNames) {
      return Promise.all(
        cacheNames.map(function (cacheName) {
          console.log('Deleting cache', cacheName);
          return caches.delete(cacheName);
        })
      );
    })
  );

  // Unregister service worker
  self.registration.unregister()
    .then(function () {
      return self.clients.matchAll();
    })
    .then(function (clients) {
      clients.forEach(client => client.navigate(client.url))
    });

});
