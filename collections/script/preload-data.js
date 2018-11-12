// load rating data
print('Loading organizations data.');
loadSeedData(db.organizations, JSON.parse(cat('collections/test/rating.json')));

// load claimant data
print('Loading organizations data.');
loadSeedData(db.categories, JSON.parse(cat('collections/test/claimant.json')));

// load claim data
print('Loading organizations data.');
loadSeedData(db.categories, JSON.parse(cat('collections/test/claim.json')));

// .. so on

function loadSeedData(collection, json) {
    json.forEach(doc => collection.insert(doc));
}
