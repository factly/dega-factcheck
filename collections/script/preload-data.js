// load rating data
print('Loading rating data.');
loadSeedData(db.rating, JSON.parse(cat('collections/test/rating.json')));

// load claimant data
print('Loading claimant data.');
loadSeedData(db.claimant, JSON.parse(cat('collections/test/claimant.json')));

// load claim data
print('Loading category data.');
loadSeedData(db.category, JSON.parse(cat('collections/test/claim.json')));

// .. so on

function loadSeedData(collection, json) {
    json.forEach(doc => collection.insert(doc));
}
